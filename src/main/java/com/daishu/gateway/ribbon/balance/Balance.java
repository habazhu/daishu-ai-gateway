package com.daishu.gateway.ribbon.balance;

import com.daishu.gateway.DaishuCloudGatewayApplication;
import com.daishu.gateway.base.DsException;
import com.daishu.gateway.ribbon.balance.Dto.BalanceContext;
import com.daishu.gateway.ribbon.balance.Dto.MatchMap;
import com.daishu.gateway.ribbon.balance.entity.Grayscale;
import com.daishu.gateway.utils.DsHeader;
import com.daishu.gateway.utils.DsStringUtil;
import com.netflix.loadbalancer.Server;
import io.jsonwebtoken.lang.Collections;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Api("负载均衡的抽象规范,暂时没有其他用途")
public abstract class Balance {
    private static Logger log = LoggerFactory.getLogger(Balance.class);
    @ApiParam("返回一台服务")
    public abstract void chooseServer(BalanceContext balanceContext);
    @ApiParam("随机返回一台服务")
    public abstract Server loadRandomServer(List<Server> serverList);


    @ApiParam("随机返回一台服务")
    protected void loadRandomServer(final @ApiParam("入参") BalanceContext balanceContext) {
        Server server= loadRandomServer(new ArrayList(balanceContext.getBalanceDto().getReachableServerMap().values()));
        balanceContext.setServer(server);
        balanceContext.setPolicyId("loadRandomServer");
    }

    @ApiParam("根据策略加载服务到context")
    protected void loadServer(final @ApiParam("灰度策略") Grayscale grayscale, final @ApiParam("入参") BalanceContext balanceContext) {
        if (Collections.isEmpty(balanceContext.getBalanceDto().getReachableServerMap())) {
            throw new DsException(10009);
        }
        MatchMap matchMap = new MatchMap();
        //判断是否命中
        effectiveStu(grayscale, balanceContext, matchMap);
        effectiveUrl(grayscale, balanceContext, matchMap);
        effectiveVer(grayscale, balanceContext, matchMap);
        //命中ip
        if (matchMap.isAllMatch()) {
            //构建context
            buildContextServer(grayscale, balanceContext);
        } else {
            //移除server
            exclusiveContextServer(grayscale, balanceContext, matchMap);
        }
    }

    @ApiParam("移除sever")
    private void exclusiveContextServer(Grayscale grayscale, BalanceContext balanceContext, @ApiParam("匹配结果") MatchMap matchMap) {
        //如果只剩下一台服务并且是一个有效的移除策略才开始移除
        if (balanceContext.getBalanceDto().getReachableServerMap().size() > 1 && DsStringUtil.isNotEmpty(grayscale.getExclusive()) && !grayscale.getExclusive().equals("*")) {
            String[] exp = grayscale.getExclusive().split(",");
            for (String str : exp) {
                switch (str) {
                    case "falseVer":
                        if (!matchMap.isVerMatch())
                            balanceContext.getBalanceDto().getReachableServerMap().remove(grayscale.getIp());
                        break;
                    case "trueVer":
                        if (matchMap.isVerMatch())
                            balanceContext.getBalanceDto().getReachableServerMap().remove(grayscale.getIp());
                        break;
                    case "trueUrl":
                        if (matchMap.isUrlMatch())
                            balanceContext.getBalanceDto().getReachableServerMap().remove(grayscale.getIp());
                        break;
                    case "falseUrl":
                        if (!matchMap.isUrlMatch())
                            balanceContext.getBalanceDto().getReachableServerMap().remove(grayscale.getIp());
                        break;
                    case "trueStu":
                        if (matchMap.isStuMatch())
                            balanceContext.getBalanceDto().getReachableServerMap().remove(grayscale.getIp());
                        break;
                    case "falseStu":
                        if (!matchMap.isStuMatch())
                            balanceContext.getBalanceDto().getReachableServerMap().remove(grayscale.getIp());
                        break;
                    default:
                }


            }
        }

    }

    @ApiParam("构建context")
    private void buildContextServer(Grayscale grayscale, BalanceContext balanceContext) {
        if (balanceContext.getBalanceDto().getReachableServerMap().get(grayscale.getIp()) != null) {
            balanceContext.setOrder(grayscale.getOrder());
            balanceContext.setPolicyId(grayscale.getId());
            balanceContext.setServer(balanceContext.getBalanceDto().getReachableServerMap().get(grayscale.getIp()));
        } else {
            if (grayscale.isEnforce()) {
                log.info("策略:" + grayscale.getId() + "没有找到服务==" + grayscale.getIp() + "强制执行失败");
                throw new DsException(10010, "策略id:" + grayscale.getId());
            } else {
                log.info("策略:" + grayscale.getId() + "没有找到服务==" + grayscale.getIp() + "跳过策略");
            }
        }
    }

    @ApiParam("匹配版本")
    private void effectiveVer(Grayscale grayscale, BalanceContext balanceContext, MatchMap matchMap) {
        //如果策略有但是header没有就不通过
        if (effective(grayscale.getVer()) && DsStringUtil.isEmpty(balanceContext.getBalanceDto().getHttpHeaders().getFirst("ver"))) {
            matchMap.setAllMatch(false);
            matchMap.setVerMatch(false);
            return;
        }
        if (effective(grayscale.getVer())) {
            DsHeader dsHeader = new DsHeader(balanceContext.getBalanceDto().getHttpHeaders());
            String ver = grayscale.getVer().replaceAll(">", "").replaceAll("=", "").replaceAll("<", "");
            //判断版本情况  header内容小于输入version版本返回-1    0 等于  header内容大于version返回1
            Integer i = dsHeader.afterVer(ver);
            //结果是小于
            if (i == -1 && !grayscale.getVer().startsWith("<")) {
                matchMap.setAllMatch(false);
                matchMap.setVerMatch(false);
            }
            //结果是大于
            if (i == 1 && !grayscale.getVer().startsWith(">")) {
                matchMap.setAllMatch(false);
                matchMap.setVerMatch(false);
            }
            //如果结果相等但是判断条件是不等于,或者不包含=
            if (i == 0 && (grayscale.getVer().startsWith("!=") || !grayscale.getVer().contains("="))) {
                matchMap.setAllMatch(false);
                matchMap.setVerMatch(false);
            }
        }
    }

    @ApiParam("判断地址匹配结果")
    private void effectiveUrl(Grayscale grayscale, BalanceContext balanceContext, MatchMap matchMap) {
        //如果需要检测url
        if (effective(grayscale.getUrl()) && !match(grayscale.getUrl(), balanceContext.getBalanceDto().getUri().toString())) {
            matchMap.setAllMatch(false);
            matchMap.setUrlMatch(false);
        }
    }

    @ApiParam("判断学生匹配结果")
    private void effectiveStu(Grayscale grayscale, BalanceContext balanceContext, MatchMap matchMap) {
        //如果策略有但是header没有
        if (effective(grayscale.getStudentId()) && DsStringUtil.isEmpty(balanceContext.getBalanceDto().getStudentId())) {
            matchMap.setAllMatch(false);
            matchMap.setStuMatch(false);
            return;
        }
        if (effective(grayscale.getStudentId()) && !match(grayscale.getStudentId(), balanceContext.getBalanceDto().getStudentId().toString())) {
            matchMap.setAllMatch(false);
            matchMap.setStuMatch(false);
        }
    }


    @ApiParam("是否是有效字段")
    private boolean effective(String str) {
        if (DsStringUtil.isEmpty(str) || str.trim().equals("*")) {
            return false;
        }
        return true;
    }

    @ApiParam("正则表达式是否匹配")
    private boolean match(@ApiParam("正则表达式") String exp, @ApiParam("内容") String str) {
        // 忽略大小写的写法
        Pattern pattern = Pattern.compile(exp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str.replaceAll("lb://","/"));
        boolean rs = matcher.matches();
        return rs;
    }

}