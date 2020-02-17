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
package org.mybatis.generator.config;

/**
 *  多对多关系
 * @author yttiany
 */
public class ManyToMany {
    /**
     * 对应的表
     */
    private String mappingTable;

    /**
     * 关联表,将主表和另外一张表关联起来
     */
    private String mappingMidTable;

    /**
     * 主表和mappingMidTable关联的字段名称
     */
    private String column;
    /**
     *
     */
    private String joinColumn;
    /**
     * 额外的查询条件
     */
    private String where;

    public ManyToMany(String mappingTable, String column)
    {
        this.mappingTable = mappingTable;
        this.column = column;
    }
    public String getMappingTable() {
        return this.mappingTable;
    }
    public void setMappingTable(String mappingTable) {
        this.mappingTable = mappingTable;
    }
    public String getColumn() {
        return this.column;
    }
    public void setColumn(String column) {
        this.column = column;
    }
    public String getJoinColumn() {
        return this.joinColumn;
    }
    public void setJoinColumn(String joinColumn) {
        this.joinColumn = joinColumn;
    }
    public String getWhere() {
        return this.where;
    }
    public void setWhere(String where) {
        this.where = where;
    }
    public String getMappingMidTable() {
        return mappingMidTable;
    }

    public void setMappingMidTable(String mappingMidTable) {
        this.mappingMidTable = mappingMidTable;
    }
}
