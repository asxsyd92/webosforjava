package com.asxsyd92;

import com.asxsyd92.annotation.TableMap;
import com.asxsyd92.utils.StringUtil;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TableConfig {
  
       private static final String FRAMEWORK_MODELPACKAGES = "com.eoaframework.models";
       private static final String APP_MODELPACKAGES = "eoaframework.app.modelpackages";
    
       public static void mapping(String DataSourceName, ActiveRecordPlugin arp)
       {
         createTableModel("com.eoaframework.models", DataSourceName, arp);
          if (StringUtil.isNotEmpty("com.asxsyd92.modle")) {
                 String[] _routesArray = "com.asxsyd92.modle".split(",");
               if ((_routesArray != null) && (_routesArray.length > 0)) {
                     for (String _routesString : _routesArray)
                             {
                             createTableModel(_routesString, DataSourceName, arp);
                             }
                       }
                 }
           }
    
       private static void createTableModel(String packageName, String DataSourceName, ActiveRecordPlugin arp)
       {
        
           Set<Class<? extends Model<?>>> _clsList = getClasses(packageName);
           // logger.info("初始化class文件的数量为：" + _clsList.size());
            if ((_clsList != null) && (_clsList.size() > 0)) {
                 for (Class<? extends Model<?>> _cls : _clsList) {
                    if (_cls.getAnnotation(TableMap.class) != null) {
                        TableMap _model = (TableMap)_cls.getAnnotation(TableMap.class);
                             if (_model.dataSourceName().equals(DataSourceName))
                                   {
                                   System.out.println("Model-DataSource数据库表映射： " + DataSourceName + "--->" + _cls.getName() + "--->" + _model.tableName());
                                  arp.addMapping(_model.tableName(), _model.primaryKey(), _cls);
                                   }
                             }
                       }
                 }
           }
    
    
    
    
    
    
    
    
    
    
    
       private static Set<Class<? extends Model<?>>> getClasses(String pack)
       {
         Set<Class<? extends Model<?>>> classes = new LinkedHashSet();
        
           boolean recursive = true;
        
           String packageName = pack;
           String packageDirName = packageName.replace('.', '/');
        
             try
                 {
               Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            
               while (dirs.hasMoreElements())
                       {
                      URL url = (URL)dirs.nextElement();
                
                     String protocol = url.getProtocol();
                
                        if ("file".equals(protocol))
                             {
                            String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    
                             findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                           } else if ("jar".equals(protocol))
                             {
                    
                    
                               try
                                   {
                        
                                    JarFile jar = ((JarURLConnection)url.openConnection()).getJarFile();
                        
                                   Enumeration<JarEntry> entries = jar.entries();
                        
                                  while (entries.hasMoreElements())
                                         {
                                        JarEntry entry = (JarEntry)entries.nextElement();
                                        String name = entry.getName();
                            
                                         if (name.charAt(0) == '/')
                                               {
                                                name = name.substring(1);
                                               }
                            
                                        if (name.startsWith(packageDirName)) {
                                              int idx = name.lastIndexOf('/');
                                
                                               if (idx != -1)
                                                     {
                                                   packageName = name.substring(0, idx).replace('/', '.');
                                                     }
                                
                                                 if ((idx != -1) || (recursive))
                                                     {
                                                   if ((name.endsWith(".class")) && (!entry.isDirectory()))
                                                           {
                                                          String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        
                                        
                                                             try
                                                                 {
                                                                 Class _cls = Class.forName(packageName + '.' + className);
                                                               if (Model.class.isAssignableFrom(_cls)) {
                                                                     classes.add(_cls);
                                                                       }
                                                                 }
                                                             catch (ClassNotFoundException e)
                                                                 {
                                                                  e.printStackTrace();
                                                                 }
                                                           }
                                                     }
                                               }
                                         }
                                   }
                               catch (IOException e) {
                                  e.printStackTrace();
                                   }
                             }
                       }
                 } catch (IOException e) {
               e.printStackTrace();
                 }
        
           return classes;
           }
    
    
    
    
    
    
    
    
    
    
       private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, boolean recursive, Set<Class<? extends Model<?>>> classes)
       {
           File dir = new File(packagePath);
        
            if ((!dir.exists()) || (!dir.isDirectory()))
                 {
                  return;
                 }
        
           File[] dirfiles = dir.listFiles(new FileFilter()
                     {
                   public boolean accept(File file)
                   {
                      return ( (file.isDirectory())) || (file.getName().endsWith(".class"));
                       }
                 });
        
          for (File file : dirfiles)
                 {
              if (file.isDirectory()) {
                    findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classes);
                       }
                   else {
                      String className = file.getName().substring(0, file.getName().length() - 6);
                
                
                
                
                         try
                             {
                            Class _cls = Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className);
                             if (Model.class.isAssignableFrom(_cls)) {
                               classes.add(_cls);
                                   }
                             }
                         catch (ClassNotFoundException e)
                             {
                              e.printStackTrace();
                             }
                       }
                 }
           }
     }

