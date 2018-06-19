import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

@Component
public class AdminLoginInterceptor {
  
  private final ApplicationContext applicationContext;

  private Map<String, String> adminUrlMap = new HashMap<>();

  @Autowired
  public AdminLoginInterceptor(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @PostConstruct
  public void handle() {
    Map<String, Object> classesMap = applicationContext.getBeansWithAnnotation(Controller.class);
    classesMap.forEach((s, o) -> {
      Class<?> c = o.getClass();
      if (c.isAnnotationPresent(AdminLogin.class)) {
        for (Method method : c.getDeclaredMethods()) {
          put(method, true);
        }
      } else {
        for (Method m : c.getDeclaredMethods()) {
          put(m, false);
        }
      }
    });
    print();
  }

  private void put(Method m, boolean notRequiredCheck) {
    if (m.isAnnotationPresent(AdminLogin.class) || notRequiredCheck) {
      Annotation a = m.getDeclaredAnnotation(GetMapping.class);
      if (null != a) {
        for (String str : ((GetMapping) a).value()) {
          adminUrlMap.put(str, "test");
        }
      }
      Annotation b = m.getDeclaredAnnotation(PostMapping.class);
      if (null != b) {
        for (String str2 : ((PostMapping) b).value()) {
          adminUrlMap.put(str2, "test");
        }
      }
      Annotation c = m.getDeclaredAnnotation(RequestMapping.class);
      if (null != c) {
        for (String str3 : ((RequestMapping) c).value()) {
          adminUrlMap.put(str3, "test");
        }
      }
    }
  }

  public void print() {
    System.out.println("-----------------------------------------------");
    adminUrlMap.forEach((s, s2) -> System.out.println(s));
  }
}

