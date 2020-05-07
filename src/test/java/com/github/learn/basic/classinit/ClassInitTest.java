package com.github.learn.basic.classinit;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.BDDAssertions.then;

/**
 * 每个测试用例只能手动单个测试（类加载）
 *
 * @author zhanfeng.zhang
 * @date 2020/5/6
 */
@Disabled
public class ClassInitTest {

    /**
     * 测试接口的初始化: 接口的初始化不会引发父接口的初始化
     */
    @Test void interfaceWillNotTriggerSuperInit() {
        FlagHolder flagHolder = SubInterface.subHolder;
        then(flagHolder.isSubInit()).isTrue();
        then(flagHolder.isSuperInit()).isFalse();
    }

    /**
     * 测试父子接口继承的初始化 通过调用 子接口.父接口的属性 只会引发父接口的初始化
     * <p>子类直接父类中的属性，只会触发父类的初始化</p>
     */
    @Test void referenceInterfaceSuperField() {
        FlagHolder h = SubInterface.superHolder;
        then(h.isSubInit()).isFalse();
        then(h.isSuperInit()).isTrue();
    }

    /**
     * 类初始化不会引发接口的初始化
     */
    @Test void classWillNotTriggerInterfaceInit() {
        FlagHolder h = SuperClass.superClassHolder;
        then(h.isSuperClassInit()).isTrue();
        then(h.isSuperInit()).isFalse();
    }

    /**
     * 类初始化会引发父类的初始化
     */
    @Test void classWillTriggerSuperClassInit() {
        FlagHolder h = SubClass.subClassHolder;
        then(h).returns(true, FlagHolder::isSubClassInit)
            .returns(true, FlagHolder::isSuperClassInit)
            .returns(false, FlagHolder::isSuperInit)
            .returns(false, from(FlagHolder::isSubInit));
    }

    /**
     * 子类直接引用父类的静态属性，只会引发父类的初始化
     * <p>子类引用父接口的静态属性，只会引发父接口的初始化</p>
     */
    @Test void classReferenceSuperClassField_thenOnlySuperClassInit() {
        FlagHolder h = SubClass.superClassHolder;
        then(h).returns(false, FlagHolder::isSubClassInit)
            .returns(true, FlagHolder::isSuperClassInit)
            .returns(false, FlagHolder::isSuperInit)
            .returns(false, from(FlagHolder::isSubInit));
    }

    /**
     * 子类直接引用父类的静态属性，只会引发父类的初始化
     * <p>子类引用父接口的静态属性，只会引发父接口的初始化</p>
     */
    @Test void classReferSuperInterfaceField_thenOnlySuperInterfaceInit() {
        FlagHolder h = SubClass.superHolder;
        then(h).returns(false, from(FlagHolder::isSubClassInit))
            .returns(false, from(FlagHolder::isSuperClassInit))
            .returns(false, from(FlagHolder::isSubInit))
            .returns(true, from(FlagHolder::isSuperInit));
    }

    /**
     * 间接引用不会引发初始化
     */
    @Test void arrayIndirectRefer_thenWillNotTriggerInit() {
        SubInterface[] array = new SubClass[5];
        then(FlagHolder.getInstance()).returns(false, from(FlagHolder::isSubClassInit))
            .returns(false, from(FlagHolder::isSuperClassInit))
            .returns(false, from(FlagHolder::isSubInit))
            .returns(false, from(FlagHolder::isSuperInit));
    }

    /**
     * 类加载但不会引发初始化
     */
    @Test void classLoadButNotInit() {
        Class<SubClass> subClassClass = SubClass.class;
        then(FlagHolder.getInstance()).returns(false, from(FlagHolder::isSubClassInit))
            .returns(false, from(FlagHolder::isSuperClassInit))
            .returns(false, from(FlagHolder::isSubInit))
            .returns(false, from(FlagHolder::isSuperInit));
    }

    /**
     * 不引发初始化
     *
     * @throws ClassNotFoundException
     */
    @Test void classLoaderLoad() throws ClassNotFoundException {
        this.getClass().getClassLoader().loadClass("com.github.learn.basic.classinit.SubClass");
        then(FlagHolder.getInstance()).returns(false, from(FlagHolder::isSubClassInit))
            .returns(false, from(FlagHolder::isSuperClassInit))
            .returns(false, from(FlagHolder::isSubInit))
            .returns(false, from(FlagHolder::isSuperInit));
    }

    /**
     * 引发初始化
     *
     * @throws ClassNotFoundException
     */
    @Test void classForName() throws ClassNotFoundException {
        // Class.forName("com.github.learn.basic.classinit.SubClass", true, this.getClass().getClassLoader());
        Class<SubClass> aClass = (Class<SubClass>)Class.forName("com.github.learn.basic.classinit.SubClass");
        then(FlagHolder.getInstance()).returns(true, from(FlagHolder::isSubClassInit))
            .returns(true, from(FlagHolder::isSuperClassInit))
            .returns(false, from(FlagHolder::isSubInit))
            .returns(false, from(FlagHolder::isSuperInit));
        this.getClass().getClassLoader().loadClass("com.github.learn.basic.classinit.SubClass");
    }

}

@Data
@Accessors(chain = true)
class FlagHolder {
    private static FlagHolder INSTANCE = new FlagHolder();
    private boolean superInit;
    private boolean subInit;

    private boolean superClassInit;
    private boolean subClassInit;

    private FlagHolder() {

    }

    public static FlagHolder getInstance() {
        return INSTANCE;
    }

}

interface SuperInterface {
    FlagHolder superHolder = FlagHolder.getInstance().setSuperInit(true);
}

interface SubInterface extends SuperInterface {
    FlagHolder subHolder = FlagHolder.getInstance().setSubInit(true);
}

class SuperClass implements SubInterface {
    static FlagHolder superClassHolder = FlagHolder.getInstance().setSuperClassInit(true);
}

class SubClass extends SuperClass implements SubInterface {
    static FlagHolder subClassHolder = FlagHolder.getInstance().setSubClassInit(true);
}

