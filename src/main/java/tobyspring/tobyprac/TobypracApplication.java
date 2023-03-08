package tobyspring.tobyprac;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
@ComponentScan
public class TobypracApplication {

    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    public static void main(String[] args) {
        SpringApplication.run(TobypracApplication.class, args);
    }

    public static void run(Class<?> applicationClass, String... args) {

        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext(){
            @Override
            protected void onRefresh() { // 주기적으로 리프레쉬하기 위함
                super.onRefresh();

                ServletWebServerFactory serverFactory = this.getBean(ServletWebServerFactory.class); //new TomcatServletWebServerFactory();
                DispatcherServlet dispatcherServlet = this.getBean(DispatcherServlet.class);
                dispatcherServlet.setApplicationContext(this);

                WebServer webserver = serverFactory.getWebServer(servletContext -> {

                    servletContext.addServlet("dispatcheerServlet",
                        dispatcherServlet// 아래에 선언한 정보들이 있으므로 this로 가져온다.
                    ).addMapping("/*");
                });

                webserver.start();
            }
        };

        //스프링 컨테이너는 어떤 클래스로 메타정보를 생성할지 정한다
//        applicationContext.registerBean(HelloController.class);
//        applicationContext.registerBean(SimpleHelloService.class);
          applicationContext.register(applicationClass);
        applicationContext.refresh();
        /*
            스프링 어노테이션과 자동설정으로 서버를 비롯한 각종 설정을 하지 않아도 웹개발이 가능하다
            따라서 내부에서 서버설정부터 시작한다
        */


            // -----dispatchservlet을 이용하지 않고 만드는 법------
                // 컨트롤러 어노테이션이 어떻게 동작하는지
//            HelloController helloController = new HelloController();

//            // 특정 url 요청이 왔을 경우 어떤식으로 응답할지 사용자가 설정하여 반환한다.
//            servletContext.addServlet("frontcontroller", new HttpServlet() {
//
//                @Override
//                protected void service(HttpServletRequest req, HttpServletResponse resp)
//                    throws ServletException, IOException {
//                    if (req.getRequestURI().equals("/hello") && req.getMethod()
//                                                                   .equals(
//                                                                       HttpMethod.GET.name())) { // GET방식일 때만 처리할 수 있다.
//                        // 쿼리 스트링의 파라미터를 받아 동적으로 반응하기 위해선 getparameter를 사용한다.
//                        String name = req.getParameter("name");
//
//                        // 사용하고자하는 빈을 불러와서 사용하면됨
//                        HelloController helloController = applicationContext.getBean(HelloController.class);
//
//                        String ret = helloController.hello(
//                            name); // 사용하고자하는 컨트롤러에 받아온 파라미터를 맵핑하고 문자로 리턴한다.
//
//                        //resp.setStatus(HttpStatus.OK.value()); // = 200을 리턴 -> 정상 결과값
//                        // 자주사용 하는 값같은 경우 ENUM으로 선언된 클래스의 값을 사용하는 것이 오타 에러를 방지할 수 있다.
//                        resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
//                        resp.getWriter().println(ret); 
//                    }  else {
//                        resp.setStatus(HttpStatus.NOT_FOUND.value()); // 404
//                    }
//
//                }
//            }).addMapping("/*"); // ex) 인증, 보안, 다국어, 공통기능 .....
//        });




    }
}

