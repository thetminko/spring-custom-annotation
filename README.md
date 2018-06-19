# spring-custom-annotation
POC of Spring custom annotation
The objective
When annotated with a custom annotation in controller class, get all the urls annotated with the custom annotation and store in the local map.


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
