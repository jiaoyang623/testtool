package guru.ioio.tool.aspect;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MethodAspect {
    private static final String TAG = MethodAspect.class.getSimpleName();

    @Before("execution(* android.app.Activity.on**(..))")
    public void onActivityMethodBefore(JoinPoint joinPoint) throws Throwable {
        String key = joinPoint.getSignature().toString();
        Log.d(TAG, "onActivityMethodBefore: " + key + "\n" + joinPoint.getThis());
    }


    @Pointcut("call(* guru.ioio.tool.tests.AspectTest.doClick(String))")
    public void interruptTest() {
    }

    @Around("interruptTest()")
    public Object replaceMethod(ProceedingJoinPoint pjp) {
        Log.i(TAG, "replaceMethod");

        return "replaceMethod";
    }
}
