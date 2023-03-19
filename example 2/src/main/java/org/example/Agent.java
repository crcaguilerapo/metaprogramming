package org.example;

import java.lang.instrument.Instrumentation;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        new AgentBuilder
                .Default()
                .type(ElementMatchers.any())
                .transform((builder, typeDescription, classLoader, module) ->
                        builder.visit(Advice.to(MyAdvice.class).on(ElementMatchers.any())))
                .installOn(inst);
    }

    public static class MyAdvice {
        @Advice.OnMethodEnter
        public static void enter(@Advice.Origin String method) {
            System.out.println("Executing method: " + method);
        }
    }
}