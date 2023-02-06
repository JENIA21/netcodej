package netcodej;

public class Result<T>{
    public T value;
    public boolean isError;
    public String errorMsg;
    public static <TValue> Result<TValue> ok(TValue value){
        var re = new Result<TValue>();
        re.value = value;
        return re;
    }
    public static <TValue> Result<TValue> fail(String errorMsg){
        var re = new Result<TValue>();
        re.isError = true;
        re.errorMsg = errorMsg;
        return re;
    }
}
