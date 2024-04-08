package com.sonnt.blog;

import com.sonnt.blog.entity.Role;
import com.sonnt.blog.repository.RoleRepostiory;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//provide information for swagger api
//using annotaion
@OpenAPIDefinition(
        info = @Info(
                title = "Spring Boot Blog App Rest Api",
                description = "Spring Boot Blog App Rest Api APIs Documentation",
                version = "v1.0.0",
                contact = @Contact(
                        name = "Nguyen Tien Son",
                        email = "sonmark19@gmail.com",
                        url = "https://www.google.com"
                ),
                license = @License(
                        name = "Apache 3.0",
                        url = "https://bard.google.com/chat/a9356bd5088c99e0"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring Boot Blog App Documentation ",
                url = "https://github.com/RameshMF/springboot-blog-rest-api/tree/main"
        )
)
//implements CommandLineRunner
public class SpringbootBlogRestApiApplication  {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootBlogRestApiApplication.class, args);
    }

    //config mapper
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }



    //Write a code insert metadata in tables whether application startup
    // spring boot will automatically create this data
    // OR YOU CAN USE TO CREATE data.sql in RESOURCE to do this
//    @Autowired
//    private RoleRepostiory roleRepostiory;
//
//    @Override
//    public void run(String... args) throws Exception {
//        Role adminRole=new Role();
//        adminRole.setName("ROLE_ADMIN");
//        roleRepostiory.save(adminRole);
//
//        Role userRole=new Role();
//        userRole.setName("ROLE_USER");
//        roleRepostiory.save(userRole);
//    }

    //http://localhost:8080/swagger-ui/index.html#/
}
