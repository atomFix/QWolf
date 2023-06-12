package com.quick.wolf.register.support.command;

import com.quick.wolf.register.support.FailBackRegistry;
import com.quick.wolf.rpc.URL;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/12/10:39
 */
public abstract class CommandFailBackRegistry extends FailBackRegistry {
    public CommandFailBackRegistry(URL url) {
        super(url);
    }


}
