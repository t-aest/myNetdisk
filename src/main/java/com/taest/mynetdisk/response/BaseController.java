package com.taest.mynetdisk.response;

import java.util.HashMap;

/**
 * @author twd
 * @description: TODO 类描述
 * @date 2021-01-12 14:06
 */
public abstract class BaseController {

    protected static Result success(){
        return new Result();
    }

    protected static Result success(Object data){
        Result result = new Result();
        result.setData(data);
        return result;
    }

    protected static Result success(ResultStatus resultStatus){
        Result result = new Result();
        result.setCode(resultStatus.getCode());
        result.setMessage(resultStatus.getMessage());
        return result;
    }

    protected static Result failure(ResultStatus resultStatus){
        Result result = new Result();
        result.setCode(resultStatus.getCode());
        result.setMessage(resultStatus.getMessage());
        return result;
    }

    protected static Result failure(ResultStatus resultStatus,Object data){
        Result result = new Result();
        result.setCode(resultStatus.getCode());
        result.setMessage(resultStatus.getMessage());
        result.setData(data);
        return result;
    }
}
