/*
 *  Copyright 2009 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package ibatortest.execute.miscellaneous;

/**
 * @author Jeff Butler
 */
public class BaseClass {
    private String lastname;
    
    /**
     * 
     */
    public BaseClass() {
        super();
    }

    // these methods are final so that an error will be generated
    // if the extended class tries to overwrite them.  This
    // will break the build as the generator should
    // see a duplicate property.
    // TODO - Make Final again once we figure out the maven classloading thing
    public String getLastname() {
        return lastname;
    }
    
    // TODO - Make Final again once we figure out the maven classloading thing
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}