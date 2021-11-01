package com.witulski.cobble;

public class Exchange {
    private ExSign coalSign;
    private ExSign ironSign;
    private ExSign goldSign;
    private ExSign diamondSign;
    private long exFee;
    private String name;
    private String registeree;

    public Exchange(String registeree, String name, long exFee){
        this.registeree = registeree;
        this.name = name;
        this.exFee = exFee;
    }

    public String getRegisteree(){ return this.registeree; }

    public String getName(){return this.name;}

    public long getExFee(){ return this.exFee; }

    public ExSign getCoalSign() { return this.coalSign; }

    public ExSign getIronSign() {
        return this.ironSign;
    }

    public ExSign getGoldSign(){
        return this.goldSign;
    }

    public ExSign getDiamondSign(){
        return this.diamondSign;
    }

    public void setCoalSign(ExSign coalSign) {
        this.coalSign = coalSign;
    }

    public void setIronSign(ExSign ironSign) {
        this.ironSign = ironSign;
    }

    public void setGoldSign(ExSign goldSign){
        this.goldSign = goldSign;
    }

    public void setDiamondSign(ExSign diamondSign){
        this.diamondSign = diamondSign;
    }

    public void setSign(ExSign sign){
        String resource = sign.getResource();
        if (resource.equals("coal")){
            setCoalSign(sign);
        }else if (resource.equals("iron")){
            setIronSign(sign);
        }else if (resource.equals("gold")){
            setGoldSign(sign);
        }else if (resource.equals("diamond")){
            setDiamondSign(sign);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
