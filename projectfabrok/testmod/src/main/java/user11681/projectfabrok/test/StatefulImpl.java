package user11681.projectfabrok.test;

import user11681.projectfabrok.annotation.Entrypoint;
import user11681.projectfabrok.annotation.Var;

@Var(name = "test", descriptor = "I")
@Var(name = "field", descriptor = "java/lang/Object")
@Entrypoint("projectfabrok:postinit")
public class StatefulImpl implements ExtraStatefulInterface {
    public static boolean bp = true;

    private int anotherThing = 123;

    public StatefulImpl() {
        {
            int thing = 123;
        }

        {
            int thing = 546;
            System.out.println("constructing.");
        }
    }

    public StatefulImpl(final int thing) {
    }

//    @Getter("test")
//    public native int getTest();
//
//    @Setter("test")
//    public native void setTest(int test);

    {
        System.out.println("nothing");
    }

    {
        System.out.println("something");
    }
}
