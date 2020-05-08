package com.github.learn.basic.classinit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/8
 */
@Slf4j
public class ClassLoaderTest {

    @Test void testClassLoader() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        then(classLoader).isSameAs(systemClassLoader);
    }

    final String JAVA_CLASS_PATH = "java.class.path";
    final String TEST_CLASS_PATH = "target/test-classes";
    final String COMPILE_CLASS_PATH = "target/classes";
    final String SUB_DIR = "com/github/learn/basic/classinit/";
    final String FILE_PATH = "tmp.txt";

    /**
     * <pre>
     * 'tmp.txt' exists in
     *     'target/classes'
     *     'target/test-classes'
     *     'target/classes/com/github/learn/basic/classinit/'
     * </pre>
     * 使用 ClassLoader 加载资源时，不能以 绝对路径 '/' 或者 './' 开头
     * <p>'tmp.txt' => load resource from the root of the classpath</p>
     * <p>'com/github/learn/basic/classinit/tmp.txt' load resource form the root of the classpath</p>
     *
     * @throws IOException
     */
    @Test void givenClassLoader_whenLoadResource() throws IOException {
        // Launcher.AppClassLoader
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        log.info("{}", "java.class.path: ");
        Arrays.stream(System.getProperty(JAVA_CLASS_PATH).split(File.pathSeparator)).forEach(System.out::println);
        if (systemClassLoader instanceof URLClassLoader) {
            log.info("{}", "Launcher.AppClassLoader path: ");
            Arrays.stream(((URLClassLoader)systemClassLoader).getURLs()).forEach(System.out::println);
        }
        // URL systemResource = ClassLoader.getSystemResource(FILE_PATH);
        URL resource = systemClassLoader.getResource(FILE_PATH);
        // search to find the first match even there are many files have the same name.
        then(resource.getFile()).contains(TEST_CLASS_PATH);
        // load resource from sub dir
        URL subDirectoryResource = systemClassLoader.getResource(SUB_DIR + FILE_PATH);
        then(subDirectoryResource.getFile()).contains(COMPILE_CLASS_PATH).contains(SUB_DIR)
            .doesNotContain(TEST_CLASS_PATH);
    }

    /**
     * <pre>
     * 'tmp.txt' exists in
     *     'target/classes'
     *     'target/test-classes'
     *     'target/classes/com/github/learn/basic/classinit/'
     * </pre>
     * 使用 Class 加载资源时
     * <p>'./tmp.txt' or 'tmp.txt' 在当前类的目录下查找</p>
     * <p>'/tmp.txt' 会在 class path 的 root 目录下查找</p>
     */
    @Test void givenClass_whenLoadResource() {
        // final String FILE_PATH = "./tmp.txt";
        // classpath:/com/github/learn/basic/classinit/InitBlock
        Class<InitBlock> initBlockClass = InitBlock.class;
        // 相对路径从类所在的目录查找，依旧会按照classpath中的顺序搜索
        // search to find the first match even there are many files have the same name.
        // first search target/test-classes and then search target/classes
        URL resource = initBlockClass.getResource(FILE_PATH);
        then(resource.getFile()).contains(COMPILE_CLASS_PATH).contains(SUB_DIR);
        // "/tmp.txt"
        URL rootResource = initBlockClass.getResource("/" + FILE_PATH);
        then(rootResource.getFile()).doesNotContain(SUB_DIR)
            .contains(TEST_CLASS_PATH);
    }

}
