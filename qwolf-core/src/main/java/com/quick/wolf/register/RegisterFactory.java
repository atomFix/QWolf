package com.quick.wolf.register;

import com.quick.wolf.core.extension.Scope;
import com.quick.wolf.core.extension.Spi;
import com.quick.wolf.rpc.URL;

/**
 * @Description:
 * @author: liukairong1
 * @date: 2023/06/09/17:50
 */
@Spi(scope = Scope.SINGLETON)
public interface RegisterFactory {

    Register getRegister(URL url);

}
