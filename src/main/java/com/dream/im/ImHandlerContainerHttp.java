package com.dream.im;

import com.dream.container.Container;
import com.dream.container.InitializeTemporaryParams;
import com.dream.container.InstanceDefinition;
import com.dream.container.utils.AnnotationResult;
import com.dream.container.utils.DreamUtils;
import com.dream.im.anno.Http;
import com.dream.im.anno.Request;
import com.dream.im.constant.RequestAnnoMember;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImHandlerContainerHttp implements Container {

    private final Map<String, ImHandlerDescHttp> container = new HashMap<>();

    @Override
    public boolean canHosting(Class<?> aClass) {
        return aClass.isAnnotationPresent(Http.class);
    }

    public ImHandlerDescHttp getHandler(String requestPath) {
        return container.get(requestPath);
    }

    @Override
    public void add(Class<?> scannedClass) throws Exception {

        Object instance = scannedClass.getConstructor().newInstance();

        Http http = scannedClass.getDeclaredAnnotation(Http.class);

        for (Method method : scannedClass.getMethods())
        {
            AnnotationResult<Request> annotation = DreamUtils.queryAnnotation(method, Request.class);
            if (annotation == null)
            {
                continue;
            }

            Request request = annotation.getParentAnnotation();

            Class<? extends Annotation> annotationType = annotation.getAnnotation().annotationType();

            Method valueMethod = annotationType.getDeclaredMethod(RequestAnnoMember.REQUEST_PATH);
            String path = (String)valueMethod.invoke(annotation);

            Method authenticateMethod = annotationType.getDeclaredMethod(RequestAnnoMember.IS_AUTHENTICATE);
            boolean authenticate = (boolean)authenticateMethod.invoke(annotation);

            String fullPath = http.value() + path;
            if (container.get(fullPath) != null)
            {
                throw new IllegalStateException("路径重复: " + fullPath);
            }

            ImHandlerDescHttp description = new ImHandlerDescHttp(method, instance, authenticate, request.method());

            container.put(fullPath, description);
        }
    }

    @Override
    public List<InstanceDefinition> getInstances() {
        HashSet<Object> instances = new HashSet<>();

        for (Map.Entry<String, ImHandlerDescHttp> entry : container.entrySet())
        {
            instances.add(entry.getValue().getInstance());
        }

        return instances.stream()
                .map(InstanceDefinition::new)
                .collect(Collectors.toList());
    }

    @Override
    public void initializeContainerParam(InitializeTemporaryParams initializeTemporaryParams) {

    }
}
