package tobyspring.tobyprac;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MySpringApplication {
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

        applicationContext.register(applicationClass);
        applicationContext.refresh();

    }

}
