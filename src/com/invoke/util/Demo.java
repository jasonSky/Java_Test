package com.invoke.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.testng.annotations.Test;

public class Demo {
	static String path = System.getProperty("user.dir") + File.separator + "test-output" + File.separator;
	
	public static void praseXml(String filepath) throws DocumentException, NoSuchMethodException, SecurityException, ClassNotFoundException, IOException{
		SAXReader reader = new SAXReader();
		File file = new File(filepath);
		Document document = reader.read(file);
		Element root = document.getRootElement().element("suite");
		String suiteName = root.attributeValue("name");
		//写入
		Document doc = DocumentHelper.createDocument();
		Element suiteNode = doc.addElement("suite");
		suiteNode.addAttribute("name", suiteName);
		suiteNode.addAttribute("verbose", "2");
		Element listener = suiteNode.addElement("listeners");
		Element reportNgListener = listener.addElement("listener");
		reportNgListener.addAttribute("class-name", "org.uncommons.reportng.HTMLReporter");
		//prase
		List<Element> childElements = root.elements("test");
		for (Element child : childElements) {
			TestObj testobj = new TestObj();
			testobj.setTestName(child.attributeValue("name"));
			List<Element> elementList = child.elements("class");
			List<ClassObj> classobjs = new ArrayList<ClassObj>();
			for (Element ele : elementList) {
				ClassObj classobj = new ClassObj();
				classobj.setClassName(ele.attributeValue("name"));
				List<Element> methodList = ele.elements("test-method");
				List<MethodObj> objs = new ArrayList<MethodObj>();
				for (Element method : methodList) {
					MethodObj obj = new MethodObj();
					obj.setMethodName(method.attributeValue("name").substring(method.attributeValue("name").lastIndexOf(".")+1));
					obj.setStatus(method.attributeValue("status"));
					obj.setIscofig(method.attributeValue("is-config"));
					obj.setDependece(method.attributeValue("depends-on-methods"));
					objs.add(obj);
				}
				classobj.setMethodObj(objs);
				classobjs.add(classobj);
			}
			testobj.setClassObj(classobjs);
			
			//处理Dependency, setup, clearup
			trancelateXml(suiteNode, testobj);
		}
		
		//去掉空的
		List selectNodes = doc.selectNodes("/suite/test/classes");
		for(int i=0;i<selectNodes.size();i++){
			Element ele = (Element) selectNodes.get(i);
			String testName = ele.getParent().attributeValue("name");
			if(ele.element("class") == null){
				Node selectSingleNode = doc.selectSingleNode("/suite/test[@name='"+testName+"']");
				System.out.println(doc.getRootElement().remove(selectSingleNode));
			}
		}
		
		//save xml
		saveXml("aa.xml", doc);
	}
	
	public static void saveXml(String fileName, Document doc) throws IOException{
		OutputFormat format = OutputFormat.createPrettyPrint();
		//设置输出编码
		format.setEncoding("UTF-8");
		//创建需要写入的File对象
		File file = new File(path+ fileName);
		//生成XMLWriter对象，构造函数中的参数为需要输出的文件流和格式
		XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
		//开始写入，write方法中包含上面创建的Document对象
		writer.write(doc);
		writer.flush();
		writer.close();
	}
	
	public static void trancelateXml(Element suiteNode, TestObj testobj) throws NoSuchMethodException, SecurityException, ClassNotFoundException{
		Element testNode = suiteNode.addElement("test");
		testNode.addAttribute("name", testobj.getTestName());
		testNode.addAttribute("preserve-order", "true");
		//class
		Element classesNode = testNode.addElement("classes");
		List<ClassObj> classObjs= testobj.getClassObj();
		for(int i=0;i<classObjs.size();i++){
			ClassObj classobj = classObjs.get(i);
			List<MethodObj> methodObjs = classobj.getMethodObj();
			String className = classobj.getClassName();
			Element classNode = classesNode.addElement("class");
			classNode.addAttribute("name", className);
			Element methodsNode = classNode.addElement("methods");
			List<String> sameMethods = new ArrayList<String>();
			int count = 0;
			for(int j=0;j<methodObjs.size();j++){
				MethodObj methodObj = methodObjs.get(j);
				if(methodObj.getIscofig()!=null && methodObj.getIscofig().equals("true") && (methodObj.getStatus().equals("SKIP") || methodObj.getStatus().equals("FAIL"))){
					//class重跑
					break;
				}else if(methodObj.getStatus().equals("SKIP") || methodObj.getStatus().equals("FAIL")){
					List<String> methods = new ArrayList<String>();
					methods.add(methodObj.getMethodName());
					sameMethods.add(methodObj.getMethodName());
					String methodP = methodObj.getDependece();
					if(methodP !=null){
						String[] dmethod = methodP.split(",");
						for(int t=0;t<dmethod.length;t++){
							String operMethod = dmethod[t].trim().substring(dmethod[t].lastIndexOf(".")+1);
							if(!sameMethods.contains(operMethod)){
								methods.add(operMethod);
								sameMethods.add(operMethod);
							}
							//反射获取
							reflectMethod(className, operMethod, methods, sameMethods);
						}
					}
					//
					for(int m=methods.size()-1;m>=0;m--){
						Element methodNode = methodsNode.addElement("include");
						methodNode.addAttribute("name", methods.get(m));
					}
				}else if(methodObj.getStatus().equals("PASS")){
					count++;
				}
			}
			
			if(count == methodObjs.size()){
				classesNode.remove(classNode);
			}
			
//			//method写xml文件
//			if(methods.size() == 0){
//				return;
//			}
//			Element methodsNode = classNode.addElement("methods");
//			for(int m=methods.size()-1;m>=0;m--){
//				Element methodNode = methodsNode.addElement("include");
//				methodNode.addAttribute("name", methods.get(m));
//			}
		}
	}
	
	//reflact
	public static void reflectMethod(String className, String methodName, List<String> methods, List<String> sameMethods) throws NoSuchMethodException, SecurityException, ClassNotFoundException{
		String[] dependsOnMethods = Class.forName(className).getMethod(methodName, null).getAnnotation(Test.class).dependsOnMethods();
		for(int k=0;k<dependsOnMethods.length;k++){
			if(!sameMethods.contains(dependsOnMethods[k])){
				sameMethods.add(dependsOnMethods[k]);
				methods.add(dependsOnMethods[k]);
			}
			methodName = dependsOnMethods[k];
			reflectMethod(className, methodName, methods, sameMethods);
		}
	}
	
	public static String generalStr(String[] args){
		String result = "";
		for(int i=0;i<args.length;i++){
			result += args[i].trim() + ",";
		}
		return result.substring(0, result.length()-1);
	}
	
	public static void main(String[] args) throws Exception {
		praseXml(path + "testng-results.xml");
 }
}