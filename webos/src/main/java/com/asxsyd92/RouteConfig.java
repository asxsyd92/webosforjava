package com.asxsyd92;

import com.jfinal.config.Routes;
 import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import org.apache.commons.lang3.StringUtils;

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

public class RouteConfig extends Routes
         {
       private static Log logger = Log.getLog(RouteConfig.class);
       public void config()
       {
             logger.info("SYS_CONTROLLERPACKAGES:  com.asxsyd92.controllers");
             createControllerRoute("com.asxsyd92.controllers");

             if (!StringUtils.isNotEmpty("com.asxsyd92.controllers")) {
                   String[] _routesArray = "com.asxsyd92.controllers".split(",");
                   logger.info("长度：" + _routesArray.length);
                   if ((_routesArray != null) && (_routesArray.length >0 )) {
                    for (String _routesString : _routesArray) {
                          System.out.println("正在创建应用包：" + _routesString + "中Controller的映射。");
                          createControllerRoute(_routesString);
                             }
                       }
                 }
           }




             
                private void createControllerRoute(String packageName) {
                    logger.info("初始化Controller-URL路由映射配置......");
                    Set<Class<Controller>> _clsList = getClasses(packageName);
                    System.out.println("初始化class文件的数量为：" + _clsList.size() + "");
                    if ((_clsList != null) && (_clsList.size() > 0)) {
                        for (Class<Controller> _cls : _clsList) {
                            if (_cls.getAnnotation(com.asxsyd92.annotation.Route.class) != null) {
                                com.asxsyd92.annotation.Route _route = (com.asxsyd92.annotation.Route) _cls.getAnnotation(com.asxsyd92.annotation.Route.class);

                                if (!_route.Key().isEmpty()) {
                                    System.out.println("Controller-URL路由映射： " + _cls.getName() + "--->" + _route.Key() + "--->" + _route.Key());
                                    System.out.println("无实际映射页面");
                                    add(_route.Key(), _cls);
                                }
                                }
                            }
                        }
                    }

                private Set<Class<Controller>> getClasses(String pack)
                {
                  Set<Class<Controller>> classes = new LinkedHashSet();
                 
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
                                                                         if (Controller.class.isAssignableFrom(_cls)) {
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
             
             
             
             
             
             
             
             
             
             
                private void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, Set<Class<Controller>> classes)
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
                                 return ((recursive) && (file.isDirectory())) || (file.getName().endsWith(".class"));
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
                                    if (Controller.class.isAssignableFrom(_cls)) {
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


