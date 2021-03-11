#负载均衡
mybalance:
  open : true
  #灰度
  grayscale:
    - order: -129
      id: ver等于2.0.0的queryPort请求,投射到9091,ver不等于2.0.0的queryPort分发到其他服
      ip: 192.168.0.225:9091
      ver: '=2.0.0'
      url: '/statistics/testServer/queryPort'
      exclusive : 'trueUrl'
    - order: -128
      id: ver>=2.4.0的所有请求,投射到9091,ver小于2.4.0分发到其他服
      ip: 192.168.0.225:9092
      ver: '>2.4.0'
      url: '*'
      exclusive : 'trueVer'
    - order: -127
      id: studentId=123的,url=queryPort 投射到9091,其他请求随机投放
      ip: 192.168.0.225:9091
      studentId: 123
      url: '/statistics/testServer/queryPort2'
      exclusive : trueUrl
    - order: -125
      id: studentId=123的,url=queryPort 投射到9091,其他请求随机投放
      ip: 192.168.0.225:9091
      studentId: 123
      url: '*'
      exclusive : '*'


测试用例说明
--------------------------------
{{base_url}}/statistics/testServer/queryPort ver 2.0.0
返回queryPort port:9091

原因:
 - order: -129 生效命中
---------------------------------
{{base_url}}/statistics/testServer/queryPort ver 2.5.0
返回queryPort port:9092 和 queryPort port:9094 随机出现
 - order: -129 生效,trueUrl将9091 从 服务列表移除
---------------------------------------------
{{base_url}}/statistics/testServer/queryPort2
返回queryPort port:9092 和 queryPort port:9094 随机出现
原因:  - order: -127 生效,trueUrl将9091 从 服务列表移除

-------------------------------------------
{{base_url}}/statistics/testServer/queryPort ver 2.6.0
{{base_url}}/statistics/testServer/queryPort2 ver 2.5.0
{{base_url}}/statistics/testServer/queryPort3  ver 2.5.0

返回 queryPort port3:9092
原因:  - order: -128 生效,直接命中

-------------------------------------------
{{base_url}}/statistics/testServer/queryPort2 studentId 123
返回 queryPort port2:9091
原因: - order: -127  生效

-------------------------------------------
{{base_url}}/statistics/testServer/queryPort3 studentId 123
返回 queryPort port3:9091
原因:  - order: -125 生效