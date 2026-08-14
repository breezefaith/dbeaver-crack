package com.dbeaver.agent;

import java.lang.instrument.Instrumentation;

import com.dbeaver.agent.intercepter.GenericInterceptor;
import com.dbeaver.agent.intercepter.LoadKeyInterceptor;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

public class Agent {
    public static void premain(String arg, Instrumentation inst) {
        hook(inst);
    }

    public static void agentmain(String arg, Instrumentation inst) {
        hook(inst);
    }

    public static void hook(Instrumentation inst) {
        System.out.println("===============DBeaver Agent 26===============");
        System.out.println("Class Interception Started");
        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            @Override
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//               System.out.println("onDiscovery: typeName=" + typeName + ", classLoader=" + classLoader + ", module=" + module + ", loaded=" + loaded);
            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
//               System.out.println("onTransformation: typeDescription=" + typeDescription + ", classLoader=" + classLoader + ", module=" + module + ", loaded=" + loaded + ", dynamicType=" + dynamicType);
            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
//                System.out.println("onIgnored: typeDescription=" + typeDescription + ", classLoader=" + classLoader + ", module=" + module + ", loaded=" + loaded);
            }

            @Override
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
               System.out.println("onError: typeName=" + typeName + ", classLoader=" + classLoader + ", module=" + module + ", loaded=" + loaded + ", throwable=" + throwable);
               throwable.printStackTrace();
            }

            @Override
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//               System.out.println("onComplete: typeName=" + typeName + ", classLoader=" + classLoader + ", module=" + module + ", loaded=" + loaded);
            }
        };
        new AgentBuilder.Default()
                // 指定需要拦截的类
                .type(ElementMatchers.nameContains("com.dbeaver.model.license.embedded.LicenseKeyProviderEmbedded"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) -> builder
                        .method(ElementMatchers.named("getDecryptionKey"))
                        .intercept(MethodDelegation.to(LoadKeyInterceptor.class)))
                .with(listener)
                .installOn(inst);
        System.out.println("Intercept LicenseDecryptionKey Done");
        new AgentBuilder.Default()
                // 指定需要拦截的类
                .type(ElementMatchers.nameContains("com.dbeaver.model.license.validate.PublicLicenseValidator"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) -> builder
                        .method(ElementMatchers.named("validateLicense"))
                        .intercept(MethodDelegation.to(new GenericInterceptor("VALID: Ok"))))
                .with(listener)
                .installOn(inst);
        System.out.println("Intercept PublicLicenseValidator Done");
        new AgentBuilder.Default()
                // 指定需要拦截的类
                .type(ElementMatchers.nameContains("com.dbeaver.model.license.validate.PublicServiceClient"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) -> builder
                        .method(ElementMatchers.named("ping"))
                        .intercept(MethodDelegation.to(new GenericInterceptor("pong")))
                        .method(ElementMatchers.named("checkCustomerEmail"))
                        .intercept(MethodDelegation.to(new GenericInterceptor("")))
                        .method(ElementMatchers.named("checkLicenseStatus"))
                        .intercept(MethodDelegation.to(new GenericInterceptor("VALID: Ok")))
                )
                .with(listener)
                .installOn(inst);
        System.out.println("Intercept LicenseCheckResult Done");
        System.out.println("Class Interception Completed");
    }
}
