/**
 *    Copyright 2006-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.plugins;

import java.util.Properties;

/**
 * ---------------------------------------------------------------------------
 * 评论插件
 * ---------------------------------------------------------------------------
 * @author: hewei
 * @time:2017/6/8 11:21
 * ---------------------------------------------------------------------------
 */
public class CommentPlugin extends BasePlugin {
    /**
     * 模板 property
     */
    public static final String PRO_TEMPLATE = "template";

    /**
     * 插件具体实现查看BasePlugin
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }
}
