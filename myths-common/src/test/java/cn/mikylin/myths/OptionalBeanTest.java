package cn.mikylin.myths;

import cn.mikylin.myths.common.bean.OptionalBean;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * simple test for {@link cn.mikylin.myths.common.bean.OptionalBean}
 *
 * @author mikylin
 * @date 20201013
 */
public class OptionalBeanTest {

    @Test
    public void test() {
        People me = new People();
        me.setAge(20);
        me.setName("mikylin");
        me.setSex(true);

        List<People> friends = new ArrayList<>();
        People he = new People();
        me.setAge(21);
        me.setName("mikylin-he");
        me.setSex(true);

        People she = new People();
        she.setAge(22);
        she.setName("mikylin-she");
        she.setSex(false);

        friends.add(he);
        friends.add(she);
        me.setFriends(friends);

//        OptionalBean<Object> of = OptionalBean.of(null);

        OptionalBean<People> meBean = OptionalBean.ofNullable(me);
        OptionalBean<Integer> integerOptionalBean = meBean.get(People::getAge);
        System.out.println(integerOptionalBean.get());
    }

}
