package com.zyx.library.hotfixlibrary;

import android.content.Context;
import com.zyx.library.hotfixlibrary.utils.Constants;
import com.zyx.library.hotfixlibrary.utils.ReflectUtils;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @author pielan
 * @date 7/3/19 上午11:22
 * @dec
 */
public class HotFixUtils {

    public static HashSet<File> loadDex = new HashSet<>();

    static {
        loadDex.clear();
    }

    /**
     * 加载热修复的dex文件
     * @param context 上下文
     */
    public static void loadFixDex(Context context) {
        if (context == null) {
            return;
        }
        File fileDir = context.getDir(Constants.DEX_DIR, Context.MODE_PRIVATE);
        File[] listFiles = fileDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(Constants.DEX_SUFFIX) && !name.equals("classes.dex");
            }
        });

        loadDex.addAll(Arrays.asList(listFiles));

        // 模拟类加载器
        createDexClassLoader(context, fileDir);
    }

    /**
     * 创建类加载补丁的DexClassLoader
     * @param context 上下文
     * @param fileDir Dex文件目录
     */
    private static void createDexClassLoader(Context context, File fileDir) {

        // 穿件临时解压目录，再加载java
        String optimizedDir = fileDir.getAbsolutePath() + File.separator + "opt_dex";
        File fopt = new File(optimizedDir);
        if (!fopt.exists()) {
            fopt.mkdirs();
        }

        for (File dex : loadDex) {
            DexClassLoader classLoader = new DexClassLoader(dex.getAbsolutePath(), optimizedDir, null, context.getClassLoader());
            hoxFix(classLoader, context);
        }

    }

    /**
     * 热修复
     * @param classLoader 自有的类加载器， 加载了修复包的DexClassLoader
     * @param context 上下文
     */
    private static void hoxFix(DexClassLoader classLoader, Context context) {
        PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
        try {
            // 获取自有的dexElements数组对象
            Object myDexElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(classLoader));

            //获取系统的的dexElements数组对象
            Object sysDexElements = ReflectUtils.getDexElements(ReflectUtils.getPathList(pathClassLoader));

            //合并后的dexElements数组对象
            Object dexElements = ReflectUtils.combileArray(myDexElements, sysDexElements);

            Object systemPathList = ReflectUtils.getPathList(pathClassLoader);

            ReflectUtils.setFiled(systemPathList, systemPathList.getClass(), dexElements);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




}
