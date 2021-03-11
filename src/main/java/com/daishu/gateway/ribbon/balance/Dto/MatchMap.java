package com.daishu.gateway.ribbon.balance.Dto;

import io.swagger.annotations.Api;

@Api("参照对象")
public class MatchMap {
    private boolean allMatch=true;
    private boolean stuMatch=true;
    private boolean urlMatch=true;
    private boolean verMatch=true;

    public boolean isAllMatch() {
        return allMatch;
    }

    public void setAllMatch(boolean allMatch) {
        this.allMatch = allMatch;
    }

    public boolean isStuMatch() {
        return stuMatch;
    }

    public void setStuMatch(boolean stuMatch) {
        this.stuMatch = stuMatch;
    }

    public boolean isUrlMatch() {
        return urlMatch;
    }

    public void setUrlMatch(boolean urlMatch) {
        this.urlMatch = urlMatch;
    }

    public boolean isVerMatch() {
        return verMatch;
    }

    public void setVerMatch(boolean verMatch) {
        this.verMatch = verMatch;
    }
}