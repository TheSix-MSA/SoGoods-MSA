package org.thesix.member.exception;

public class BadRequestException extends RuntimeException{

    private String errCode;

    /**
     * 400 Err
     */
    public BadRequestException(){
        this.errCode = "400";
    }

    /**
     * 9300 Err
     * NullPoint
     * @Param code 에러코드
     */
    public BadRequestException(String code){
        this.errCode = code;
    }
}
