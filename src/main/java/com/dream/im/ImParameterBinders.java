package com.dream.im;

import com.dream.container.Container;
import com.dream.container.InitializeTemporaryParams;
import com.dream.container.InstanceDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImParameterBinders implements Container {

    private final Map<Class<?>, ImParameterBind<ImMessage>> container = new HashMap<>();

    public ImParameterBind<ImMessage> getBinder(Class<?> clazz)
    {
        return container.get(clazz);
    }

    @Override
    public boolean canHosting(Class<?> aClass)
    {
        return ImParameterBind.class.isAssignableFrom(aClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void add(Class<?> aClass) throws Exception
    {
        if (ImParameterBind.class.isAssignableFrom(aClass))
        {
            container.put(aClass, (ImParameterBind<ImMessage>) aClass.newInstance());
        }
    }

    @Override
    public List<InstanceDefinition> getInstances()
    {
        List<InstanceDefinition> defs = new ArrayList<>();

        for (Map.Entry<Class<?>, ImParameterBind<ImMessage>> entry : container.entrySet())
        {
            InstanceDefinition definition = new InstanceDefinition();
            definition.setOriginalInstance(entry.getValue());
            defs.add(definition);
        }

        return defs;
    }

    @Override
    public void initializeContainerParam(InitializeTemporaryParams initializeTemporaryParams)
    {
    }
}
