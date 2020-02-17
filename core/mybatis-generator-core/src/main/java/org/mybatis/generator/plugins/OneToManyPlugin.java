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
        for (OneToMany oneToMany : introspectedTable.getOneToManys()) {
            String tableName = oneToMany.getMappingTable();
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
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        Context context = introspectedTable.getContext();
        String patTableName= introspectedTable.getFullyQualifiedTable().toString();
        for (OneToMany oneToMany : introspectedTable.getOneToManys()) {
            String tableName = oneToMany.getMappingTable();
            String midTableName = oneToMany.getMappingMidTable();
            TableConfiguration tc = getMapTc(tableName, context);
            TableConfiguration midTc = getMapTc(midTableName, context);
            IntrospectedTable it = getIt(tableName, context);

            if( tc != null && null == midTc ){
                generateSQLMapByTable(tc,oneToMany,document,patTableName);
            }else if(null != tc  &&  null != midTc){
                generateSQLMapByTableAndMidTable(tc,midTc,oneToMany,document,patTableName);
            }

//            if (tc != null && null ==midTc ) {
//                String domainName = tc.getDomainObjectName();
//                if ( !stringHasValue(domainName)) {
//                    if( stringHasValue(tableName) ){
//                        domainName =  JavaBeansUtil.getCamelCaseString(tableName, true);
//                        tc.setDomainObjectName(domainName);
//                    }
//                }
//                domainName=domainName + "s";
//                String fieldName = domainName.replaceFirst(new String(new char[] { domainName.charAt(0) }), new String(new char[] { domainName.charAt(0) }).toLowerCase());
//
//                XmlElement assEle = new XmlElement("collection");
//                assEle.addAttribute(new Attribute("property", fieldName));
//                assEle.addAttribute(new Attribute("column", otm.getColumn()));
//                assEle.addAttribute(new Attribute("select", "get" + domainName));
////                for (Element ele : document.getRootElement().getOldElements()) {
//                for ( VisitableElement ele : document.getRootElement().getElements() ) {
//                    XmlElement xe = (XmlElement)ele;
//                    for ( Attribute a:xe.getAttributes() ) {
//                        if ((a.getName().equalsIgnoreCase("id")) && ("BaseResultMap".equals(a.getValue()))){
//                            xe.addElement(assEle);
//                        }
//                    }
//                }
//                Iterator localIterator3;
//                String tuofengColum = "";
//                boolean isUp = false;
//                byte[] xe = otm.getColumn().getBytes();
//                int localAttribute1 = xe.length;
//                for (int a = 0; a < localAttribute1; a++) {
//                    byte b = xe[a];
//                    char c = (char)b;
//                    if (c == '_') {
//                        isUp = true;
//                    }
//                    else if (isUp) {
//                        tuofengColum = tuofengColum + new String(new char[] { c }).toUpperCase();
//                        isUp = false;
//                    } else {
//                        tuofengColum = tuofengColum + c;
//                    }
//
//                }
//
//                XmlElement selectEle = new XmlElement("select");
//                selectEle.addAttribute(new Attribute("id", "get" + domainName));
//                selectEle.addAttribute(new Attribute("resultMap", it.getMyBatis3SqlMapNamespace() + ".BaseResultMap"));
//                String sql = "select ";
//                for (IntrospectedColumn c : it.getAllColumns()) {
//                    sql = sql +"ct."+ c.getActualColumnName() + ",";
//                }
//                sql = sql.substring(0, sql.length() - 1);
//                sql = sql + " \n\t from " + tableName + " as ct \n\t\t right join  "+patTableName+" as pt on ct."+otm.getColumn()+"= pt."+otm.getColumn()+" \n\t where ct." + otm.getJoinColumn() + "=#{" + tuofengColum + "} ";
//                if (StringUtility.stringHasValue(otm.getWhere())) {
//                    sql = sql + " and " + otm.getWhere();
//                }
//                selectEle.addElement(new TextElement(sql));
//                document.getRootElement().addElement(selectEle);
//            }
        }
//        return super.sqlMapDocumentGenerated(document, introspectedTable);
        return true;
    }

    /**
     *  一对多关系,直接两个表做关联的情况
     * @param tc 从表TableConfiguration 对象
     * @param oneToMany oneToMany 对象
     * @param document sql生成的doc对象
     * @param patTableName 主表表名
     */
    private void generateSQLMapByTable( TableConfiguration tc,OneToMany oneToMany,Document document,String patTableName){
            String tableName = oneToMany.getMappingTable();
            IntrospectedTable it = getIt(tableName, context);
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
            assEle.addAttribute(new Attribute("column", oneToMany.getColumn()));
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
            byte[] xe = oneToMany.getColumn().getBytes();
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
            String sql = " \n\t select ";
            for (IntrospectedColumn c : it.getAllColumns()) {
                sql = sql +" \n\t\t ct."+ c.getActualColumnName() + ",";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql = sql + " \n\t from " + tableName + " as ct \n\t\t right join  "+patTableName+" as pt on ct."+oneToMany.getJoinColumn()+" = pt."+oneToMany.getColumn();
            if (StringUtility.stringHasValue(oneToMany.getWhere())) {
                sql = sql + "\n\t where " + oneToMany.getWhere();
            }
            selectEle.addElement(new TextElement(sql));
            document.getRootElement().addElement(selectEle);
    }

    /**
     *
     *  一对多关系,通过关联表midTable做三个表做关联的情况
     * @param tc 从表TableConfiguration 对象
     * @param oneToMany oneToMany 对象
     * @param document sql生成的doc对象
     * @param midTc 中间关联表TableConfiguration 对象
     * @param parentTableName 主表表名
     */
    private void generateSQLMapByTableAndMidTable( TableConfiguration tc,TableConfiguration midTc,OneToMany oneToMany,Document document,String parentTableName){
        String childTableName = oneToMany.getMappingTable();
        IntrospectedTable it = getIt(childTableName, context);
        String domainName = tc.getDomainObjectName();

        String midTableName = oneToMany.getMappingTable();
        IntrospectedTable midIt = getIt(midTableName, context);
        if ( !stringHasValue(domainName)) {
            if( stringHasValue(childTableName) ){
                domainName =  JavaBeansUtil.getCamelCaseString(childTableName, true);
                tc.setDomainObjectName(domainName);
            }
        }
        domainName=domainName + "s";
        String fieldName = domainName.replaceFirst(new String(new char[] { domainName.charAt(0) }), new String(new char[] { domainName.charAt(0) }).toLowerCase());

        XmlElement assEle = new XmlElement("collection");
        assEle.addAttribute(new Attribute("property", fieldName));
        assEle.addAttribute(new Attribute("column", oneToMany.getColumn()));
        assEle.addAttribute(new Attribute("select", "get" + domainName));

        for ( VisitableElement ele : document.getRootElement().getElements() ) {
            XmlElement xe = (XmlElement)ele;
            for ( Attribute a:xe.getAttributes() ) {
                if ((a.getName().equalsIgnoreCase("id")) && ("BaseResultMap".equals(a.getValue()))){
                    xe.addElement(assEle);
                }
            }
        }
        Iterator localIterator3;


        XmlElement selectEle = new XmlElement("select");
        selectEle.addAttribute(new Attribute("id", "get" + domainName));
        selectEle.addAttribute(new Attribute("resultMap", it.getMyBatis3SqlMapNamespace() + ".BaseResultMap"));
        String sql = " \n\t select ";
        for (IntrospectedColumn c : it.getAllColumns()) {
            sql = sql +"\n\t\tct."+ c.getActualColumnName() + ",";
        }
        sql = sql.substring(0, sql.length() - 1);
        StringBuffer sqlBuffer = new StringBuffer(sql);
        sqlBuffer.append(" \n\t from " + childTableName + " as ct ");
        sqlBuffer.append("\n\t\t right join  "+ midTableName +" as mid ");
        sqlBuffer.append("\n\t\t inner join  "+ parentTableName +" as pt on pt."+oneToMany.getColumn()+" = mid." + oneToMany.getMidPTJoinName() );
        sqlBuffer.append("on ct."+oneToMany.getJoinColumn()+" = mid."+oneToMany.getMidCTJoinName());
//        sql = sql + " \n\t from " + childTableName + " as ct \n\t\t right join  "+parentTableName+" as pt on ct."+oneToMany.getColumn()+"= pt."+oneToMany.getJoinColumn();
        if (StringUtility.stringHasValue(oneToMany.getWhere())) {
//            sql = sql + " and " + oneToMany.getWhere();
            sqlBuffer.append(" \n\t where "+oneToMany.getWhere());
        }
        selectEle.addElement(new TextElement(sqlBuffer.toString()));
        document.getRootElement().addElement(selectEle);
    }

    @Override
    public boolean validate(List<String> warnings)
    {
        return true;
    }

    /**
     * 将字段做驼峰转换后返回
     * @param colunmName  原始属性
     * @return 驼峰处理以后的属性名
     */
    private String getTuoFengColumnName(String colunmName){
        String tuofengColum = "";
        boolean isUp = false;
        byte[] xe = colunmName.getBytes();
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

        return tuofengColum;
    }
}
