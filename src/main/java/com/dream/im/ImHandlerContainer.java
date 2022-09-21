package com.dream.im;

import com.dream.container.Container;
import com.dream.container.InitializeTemporaryParams;
import com.dream.container.InstanceDefinition;
import com.dream.im.anno.HandleMethod;
import com.dream.im.anno.Handler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImHandlerContainer implements Container {

    private final Map<String, ImHandlerDesc> container = new HashMap<>();

    @Override
    public boolean canHosting(Class<?> aClass) {
        return aClass.isAnnotationPresent(Handler.class);
    }

    public ImHandlerDesc getHandler(String handleMethodName) {
        return container.get(handleMethodName);
    }

    @Override
    public void add(Class<?> scannedClass) throws Exception {
        Object instance = scannedClass.getConstructor().newInstance();

        for (Method method : scannedClass.getMethods())
        {
            HandleMethod handle = method.getDeclaredAnnotation(HandleMethod.class);
            if (handle == null)
            {
                continue;
            }

            String methodValue = handle.value();
            if (container.get(methodValue) != null)
            {
                throw new RuntimeException("Repeated Method " + methodValue);
            }

            ImHandlerDesc description = new ImHandlerDesc(method, instance, methodValue, handle.authenticate());

            container.put(methodValue, description);
        }
    }

    @Override
    public List<InstanceDefinition> getInstances() {
        HashSet<Object> instances = new HashSet<>();

        for (Map.Entry<String, ImHandlerDesc> entry : container.entrySet())
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
