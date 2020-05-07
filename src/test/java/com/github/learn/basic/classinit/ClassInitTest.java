package com.github.learn.basic.classinit;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.BDDAssertions.then;

/**
 * 每个测试用例只能单个测试（类加载）
 *
 * @author zhanfeng.zhang
 * @date 2020/5/6
 */
@Disabled
public class ClassInitTest {

    /**
     * 每次测试前都重新初始化一次 FlagHolder, 使各个 UT 互不干扰
     */
    @BeforeEach
    void initFlagHolder() {
        // 初始化 holder
        FlagHolder.initInstance();
    }

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

}

@Data
@Accessors(chain = true)
class FlagHolder {
    private static FlagHolder INSTANCE;
    private boolean superInit;
    private boolean subInit;

    private boolean superClassInit;
    private boolean subClassInit;

    private FlagHolder() {

    }

    public static final void initInstance() {
        INSTANCE = new FlagHolder();
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

