package test;

import elidation.Callings;
import elidation.ValidateFunction;

import java.time.LocalDate;

/**
 * Created by earayu on 2016/8/21.
 */
public class MyCallings implements Callings
{

    public ValidateFunction id()
    {
        ValidateFunction validateFunction =
                (str)->
                {
                    System.out.println("ads");
                    System.out.println(str);
                };
        return validateFunction;
    }

    public ValidateFunction now()
    {
        return (str)->{
            System.out.println("last year:" + LocalDate.now().minusYears(1).getYear());
        };
    }

}
