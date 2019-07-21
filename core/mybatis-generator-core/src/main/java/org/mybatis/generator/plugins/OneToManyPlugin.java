/**
 *    Copyright 2006-2019 the original author or authors.
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

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.*;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.OneToMany;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Iterator;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @author yttiany
 * @author yttiany
 */
public class OneToManyPlugin extends PluginAdapter {

    private TableConfiguration getMapTc(String tableName, Context context)
    {
        TableConfiguration tc = null;
        for (TableConfiguration t : context.getTableConfigurations()) {
            if (t.getTableName().equalsIgnoreCase(tableName)) {
                tc = t;
            }
        }
        return tc;
    }

    private IntrospectedTable getIt(String tableName, Context context) {
        for (IntrospectedTable i : context.getIntrospectedTables()) {
            i.calculateJavaClientAttributes();
            i.calculateXmlAttributes();
            if (i.getTableConfiguration().getTableName().equalsIgnoreCase(tableName)) {
                return i;
            }
        }
        return null;
    }


    private String getModelPackage(IntrospectedTable introspectedTable, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getJavaModelGeneratorConfiguration().getTargetPackage());
        sb.append(introspectedTable.getFullyQualifiedTable().getSubPackageForModel(StringUtility.isTrue(context.getJavaModelGeneratorConfiguration().getProperty("enableSubPackages"))));
        String pakkage = sb.toString();
        return pakkage;
    }
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        Context context = introspectedTable.getContext();
        for (OneToMany otm : introspectedTable.getOneToManys()) {
            String tableName = otm.getMappingTable();
            TableConfiguration tc = getMapTc(tableName, context);
            if (tc != null) {
                String pakkage = getModelPackage(introspectedTable, context);
                String domainName = tc.getDomainObjectName();
                if ( !stringHasValue(domainName)) {
                    if( stringHasValue(tableName) ){
                        domainName =  JavaBeansUtil.getCamelCaseString(tableName, true);
                        tc.setDomainObjectName(domainName);
                    }
                }
                String type = pakkage + "." + domainName;
                String fieldName = domainName.replaceFirst(new String(new char[] { domainName.charAt(0) }), new String(new char[] { domainName.charAt(0) }).toLowerCase()) + "s";
                Field field = new Field();

                field.setName(fieldName);
                FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.util.List<" + type + ">");
                field.setType(fqjt);
                field.setVisibility(JavaVisibility.PRIVATE);
                topLevelClass.addImportedType(new FullyQualifiedJavaType(type));
                topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.List"));
                topLevelClass.addField(field);

                Method getMethod = new Method();
                getMethod.setVisibility(JavaVisibility.PUBLIC);
                getMethod.setReturnType(fqjt);
                getMethod.setName("get" + tc.getDomainObjectName() + "s");
                getMethod.addBodyLine("return " + fieldName + ";");
                topLevelClass.addMethod(getMethod);

                Method setMethod = new Method();
                setMethod.setVisibility(JavaVisibility.PUBLIC);
                setMethod.setName("set" + tc.getDomainObjectName() + "s");
                setMethod.addParameter(new Parameter(fqjt, fieldName));
                setMethod.addBodyLine("this." + fieldName + "=" + fieldName + ";");
                topLevelClass.addMethod(setMethod);
            }
        }
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable)
    {
        Context context = introspectedTable.getContext();
        for (OneToMany otm : introspectedTable.getOneToManys()) {
            String tableName = otm.getMappingTable();
            TableConfiguration tc = getMapTc(tableName, context);
            IntrospectedTable it = getIt(tableName, context);
            if (tc != null) {
                String domainName = tc.getDomainObjectName();
                if ( !stringHasValue(domainName)) {
                    if( stringHasValue(tableName) ){
                        domainName =  JavaBeansUtil.getCamelCaseString(tableName, true);
                        tc.setDomainObjectName(domainName);
                    }
                }
                domainName=domainName + "s";
                String fieldName = domainName.replaceFirst(new String(new char[] { domainName.charAt(0) }), new String(new char[] { domainName.charAt(0) }).toLowerCase());

                XmlElement assEle = new XmlElement("collection");
                assEle.addAttribute(new Attribute("property", fieldName));
                assEle.addAttribute(new Attribute("column", otm.getColumn()));
                assEle.addAttribute(new Attribute("select", "get" + domainName));
//                for (Element ele : document.getRootElement().getOldElements()) {
                for ( VisitableElement ele : document.getRootElement().getElements() ) {
                    XmlElement xe = (XmlElement)ele;
                    for ( Attribute a:xe.getAttributes() ) {
                        if ((a.getName().equalsIgnoreCase("id")) && ("BaseResultMap".equals(a.getValue()))){
                            xe.addElement(assEle);
                        }
                    }
                }
                Iterator localIterator3;
                String tuofengColum = "";
                boolean isUp = false;
                byte[] xe = otm.getColumn().getBytes();
                int localAttribute1 = xe.length;
                for (int a = 0; a < localAttribute1; a++) {
                    byte b = xe[a];
                    char c = (char)b;
                    if (c == '_') {
                        isUp = true;
                    }
                    else if (isUp) {
                        tuofengColum = tuofengColum + new String(new char[] { c }).toUpperCase();
                        isUp = false;
                    } else {
                        tuofengColum = tuofengColum + c;
                    }

                }

                XmlElement selectEle = new XmlElement("select");
                selectEle.addAttribute(new Attribute("id", "get" + domainName));
                selectEle.addAttribute(new Attribute("resultMap", it.getMyBatis3SqlMapNamespace() + ".BaseResultMap"));
                String sql = "select ";
                for (IntrospectedColumn c : it.getAllColumns()) {
                    sql = sql + c.getActualColumnName() + ",";
                }
                sql = sql.substring(0, sql.length() - 1);
                sql = sql + " from " + tableName + " where " + otm.getJoinColumn() + "=#{" + tuofengColum + "} ";
                if (StringUtility.stringHasValue(otm.getWhere())) {
                    sql = sql + " and " + otm.getWhere();
                }
                selectEle.addElement(new TextElement(sql));
                document.getRootElement().addElement(selectEle);
            }
        }
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean validate(List<String> warnings)
    {
        return true;
    }
}
