package com.dream.im;

import com.dream.container.Initializer;
import com.dream.container.anno.InitializeArgs;

@InitializeArgs(
        containers = {
                ImHandlerContainer.class,
                ImParameterBinders.class,
                ImHandlerContainerHttp.class
        }
)
public class ImLauncher
{

    public static void main(String[] args)
    {
        Initializer.initialize(ImLauncher.class, args);
    }
}
