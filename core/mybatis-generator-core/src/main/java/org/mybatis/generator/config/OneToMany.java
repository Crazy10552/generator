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
 * @author yttiany
 * @author yttiany
 */
public class OneToMany {
    /**
     * 对应的表
     */
    private String mappingTable;

    /**
     * 关联表,将主表和另外一张表关联起来
     */
    private String mappingMidTable;
    /**
     * 主表和中间表对应的字段属性
     */
    private String midPTJoinName;

    /**
     * 子表和中间表对应的字段属性
     */
    private String midCTJoinName;

    /**
     * 主表和mappingMidTable关联的字段名称
     */
    private String column;
    /**
     * 子表和主表对应的字段信息
     */
    private String joinColumn;
    /**
     * 额外的查询条件(这个用法还没怎么细化,建议先不要用)
     */
    private String where;

    public OneToMany(String mappingTable, String column)
    {
        this.mappingTable = mappingTable;
        this.column = column;
    }

    public OneToMany(String mappingTable, String mappingMidTable, String midPTJoinName, String midCTJoinName, String column, String joinColumn, String where) {
        this.mappingTable = mappingTable;
        this.mappingMidTable = mappingMidTable;
        this.midPTJoinName = midPTJoinName;
        this.midCTJoinName = midCTJoinName;
        this.column = column;
        this.joinColumn = joinColumn;
        this.where = where;
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
    public String getMidPTJoinName() {
        return midPTJoinName;
    }
    public void setMidPTJoinName(String midPTJoinName) {
        this.midPTJoinName = midPTJoinName;
    }
    public String getMidCTJoinName() {
        return midCTJoinName;
    }
    public void setMidCTJoinName(String midCTJoinName) {
        this.midCTJoinName = midCTJoinName;
    }
}
