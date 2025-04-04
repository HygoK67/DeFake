package com.group6.defakelogibackend;

import com.group6.defakelogibackend.mapper.UserMapper;
import com.group6.defakelogibackend.model.User;
import com.group6.defakelogibackend.utils.EmailService;
import com.group6.defakelogibackend.utils.PasswordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class DefakeLogiBackendApplicationTests {

    @Autowired
    EmailService emailService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordService passwordService;

    @Test
    void contextLoads() {
        String pass = "asdasd";
        User user0 = new User();
        user0.setUsername("test-user");
        user0.setEmail("example@example.com");
        user0.setPasswordHash(passwordService.encodePassword(pass));
        userMapper.addUser(user0);
    }

}
/*

   MybatisLanguageDriverAutoConfiguration.ThymeleafConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.mybatis.scripting.thymeleaf.ThymeleafLanguageDriver' (OnClassCondition)

   MybatisLanguageDriverAutoConfiguration.VelocityConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.mybatis.scripting.velocity.VelocityLanguageDriver', 'org.mybatis.scripting.velocity.VelocityLanguageDriverConfig' (OnClassCondition)

   Neo4jAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.neo4j.driver.Driver' (OnClassCondition)

   Neo4jDataAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.neo4j.driver.Driver' (OnClassCondition)

   Neo4jReactiveDataAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.neo4j.driver.Driver' (OnClassCondition)

   Neo4jReactiveRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.neo4j.driver.Driver' (OnClassCondition)

   Neo4jRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.neo4j.driver.Driver' (OnClassCondition)

   NettyAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'io.netty.util.NettyRuntime' (OnClassCondition)

   OAuth2AuthorizationServerAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.oauth2.server.authorization.OAuth2Authorization' (OnClassCondition)

   OAuth2AuthorizationServerJwtAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.oauth2.server.authorization.OAuth2Authorization' (OnClassCondition)

   OAuth2ClientAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.config.annotation.web.configuration.EnableWebSecurity' (OnClassCondition)

   OAuth2ResourceServerAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken' (OnClassCondition)

   ProjectInfoAutoConfiguration#buildProperties:
      Did not match:
         - @ConditionalOnResource did not find resource '${spring.info.build.location:classpath:META-INF/build-info.properties}' (OnResourceCondition)

   ProjectInfoAutoConfiguration#gitProperties:
      Did not match:
         - GitResource did not find git info at classpath:git.properties (ProjectInfoAutoConfiguration.GitResourceAvailableCondition)

   PulsarAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.apache.pulsar.client.api.PulsarClient' (OnClassCondition)

   PulsarReactiveAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.apache.pulsar.client.api.PulsarClient' (OnClassCondition)

   QuartzAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.quartz.Scheduler' (OnClassCondition)

   R2dbcAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'io.r2dbc.spi.ConnectionFactory' (OnClassCondition)

   R2dbcDataAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.data.r2dbc.core.R2dbcEntityTemplate' (OnClassCondition)

   R2dbcInitializationConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required classes 'io.r2dbc.spi.ConnectionFactory', 'org.springframework.r2dbc.connection.init.DatabasePopulator' (OnClassCondition)

   R2dbcProxyAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'io.r2dbc.proxy.ProxyConnectionFactory' (OnClassCondition)

   R2dbcRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'io.r2dbc.spi.ConnectionFactory' (OnClassCondition)

   R2dbcTransactionManagerAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.r2dbc.connection.R2dbcTransactionManager' (OnClassCondition)

   RSocketGraphQlClientAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'graphql.GraphQL' (OnClassCondition)

   RSocketMessagingAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'io.rsocket.RSocket' (OnClassCondition)

   RSocketRequesterAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'io.rsocket.RSocket' (OnClassCondition)

   RSocketSecurityAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.rsocket.core.SecuritySocketAcceptorInterceptor' (OnClassCondition)

   RSocketServerAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'io.rsocket.core.RSocketServer' (OnClassCondition)

   RSocketStrategiesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'io.netty.buffer.PooledByteBufAllocator' (OnClassCondition)

   RabbitAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.rabbitmq.client.Channel' (OnClassCondition)

   ReactiveElasticsearchClientAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'co.elastic.clients.transport.ElasticsearchTransport' (OnClassCondition)

   ReactiveElasticsearchRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'reactor.core.publisher.Mono' (OnClassCondition)

   ReactiveMultipartAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.web.reactive.config.WebFluxConfigurer' (OnClassCondition)

   ReactiveOAuth2ClientAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'reactor.core.publisher.Flux' (OnClassCondition)

   ReactiveOAuth2ResourceServerAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity' (OnClassCondition)

   ReactiveSecurityAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'reactor.core.publisher.Flux' (OnClassCondition)

   ReactiveUserDetailsServiceAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.authentication.ReactiveAuthenticationManager' (OnClassCondition)

   ReactiveWebServerFactoryAutoConfiguration:
      Did not match:
         - @ConditionalOnWebApplication did not find reactive web application classes (OnWebApplicationCondition)

   ReactorAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'reactor.core.publisher.Hooks' (OnClassCondition)

   RedisAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.data.redis.core.RedisOperations' (OnClassCondition)

   RedisCacheConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.data.redis.connection.RedisConnectionFactory' (OnClassCondition)

   RedisReactiveAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'reactor.core.publisher.Flux' (OnClassCondition)

   RedisRepositoriesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.data.redis.repository.configuration.EnableRedisRepositories' (OnClassCondition)

   RepositoryRestMvcAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration' (OnClassCondition)

   Saml2RelyingPartyAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository' (OnClassCondition)

   SecurityAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.authentication.DefaultAuthenticationEventPublisher' (OnClassCondition)

   SecurityFilterAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.config.http.SessionCreationPolicy' (OnClassCondition)

   SendGridAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'com.sendgrid.SendGrid' (OnClassCondition)

   ServletWebServerFactoryAutoConfiguration.ForwardedHeaderFilterConfiguration:
      Did not match:
         - @ConditionalOnProperty (server.forward-headers-strategy=framework) did not find property 'server.forward-headers-strategy' (OnPropertyCondition)

   ServletWebServerFactoryConfiguration.EmbeddedJetty:
      Did not match:
         - @ConditionalOnClass did not find required classes 'org.eclipse.jetty.server.Server', 'org.eclipse.jetty.util.Loader', 'org.eclipse.jetty.ee10.webapp.WebAppContext' (OnClassCondition)

   ServletWebServerFactoryConfiguration.EmbeddedUndertow:
      Did not match:
         - @ConditionalOnClass did not find required classes 'io.undertow.Undertow', 'org.xnio.SslClientAuthMode' (OnClassCondition)

   SessionAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.session.Session' (OnClassCondition)

   SpringApplicationAdminJmxAutoConfiguration:
      Did not match:
         - @ConditionalOnProperty (spring.application.admin.enabled=true) did not find property 'enabled' (OnPropertyCondition)

   SpringDataWebAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.data.web.PageableHandlerMethodArgumentResolver' (OnClassCondition)

   TaskExecutorConfigurations.SimpleAsyncTaskExecutorBuilderConfiguration#simpleAsyncTaskExecutorBuilderVirtualThreads:
      Did not match:
         - @ConditionalOnMissingBean (types: org.springframework.boot.task.SimpleAsyncTaskExecutorBuilder; SearchStrategy: all) found beans of type 'org.springframework.boot.task.SimpleAsyncTaskExecutorBuilder' simpleAsyncTaskExecutorBuilder (OnBeanCondition)

   TaskExecutorConfigurations.TaskExecutorConfiguration#applicationTaskExecutorVirtualThreads:
      Did not match:
         - @ConditionalOnThreading did not find VIRTUAL (OnThreadingCondition)

   TaskSchedulingAutoConfiguration#scheduledBeanLazyInitializationExcludeFilter:
      Did not match:
         - @ConditionalOnBean (names: org.springframework.context.annotation.internalScheduledAnnotationProcessor; SearchStrategy: all) did not find any beans named org.springframework.context.annotation.internalScheduledAnnotationProcessor (OnBeanCondition)

   TaskSchedulingConfigurations.SimpleAsyncTaskSchedulerBuilderConfiguration#simpleAsyncTaskSchedulerBuilderVirtualThreads:
      Did not match:
         - @ConditionalOnMissingBean (types: org.springframework.boot.task.SimpleAsyncTaskSchedulerBuilder; SearchStrategy: all) found beans of type 'org.springframework.boot.task.SimpleAsyncTaskSchedulerBuilder' simpleAsyncTaskSchedulerBuilder (OnBeanCondition)

   TaskSchedulingConfigurations.TaskSchedulerConfiguration:
      Did not match:
         - @ConditionalOnBean (names: org.springframework.context.annotation.internalScheduledAnnotationProcessor; SearchStrategy: all) did not find any beans named org.springframework.context.annotation.internalScheduledAnnotationProcessor (OnBeanCondition)

   ThymeleafAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.thymeleaf.spring6.SpringTemplateEngine' (OnClassCondition)

   TransactionAutoConfiguration#transactionalOperator:
      Did not match:
         - @ConditionalOnSingleCandidate (types: org.springframework.transaction.ReactiveTransactionManager; SearchStrategy: all) did not find any beans (OnBeanCondition)

   TransactionAutoConfiguration.AspectJTransactionManagementConfiguration:
      Did not match:
         - @ConditionalOnBean did not find required type 'org.springframework.transaction.aspectj.AbstractTransactionAspect' (OnBeanCondition)
         - @ConditionalOnBean (types: org.springframework.transaction.aspectj.AbstractTransactionAspect; SearchStrategy: all) did not find any beans of type org.springframework.transaction.aspectj.AbstractTransactionAspect (OnBeanCondition)

   TransactionAutoConfiguration.EnableTransactionManagementConfiguration.JdkDynamicAutoProxyConfiguration:
      Did not match:
         - @ConditionalOnProperty (spring.aop.proxy-target-class=false) did not find property 'proxy-target-class' (OnPropertyCondition)

   UserDetailsServiceAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.security.authentication.AuthenticationManager' (OnClassCondition)

   ValidationAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'jakarta.validation.executable.ExecutableValidator' (OnClassCondition)

   WebClientAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.web.reactive.function.client.WebClient' (OnClassCondition)

   WebFluxAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.web.reactive.config.WebFluxConfigurer' (OnClassCondition)

   WebMvcAutoConfiguration#hiddenHttpMethodFilter:
      Did not match:
         - @ConditionalOnProperty (spring.mvc.hiddenmethod.filter.enabled) did not find property 'enabled' (OnPropertyCondition)

   WebMvcAutoConfiguration.ProblemDetailsErrorHandlingConfiguration:
      Did not match:
         - @ConditionalOnProperty (spring.mvc.problemdetails.enabled=true) did not find property 'enabled' (OnPropertyCondition)

   WebMvcAutoConfiguration.ResourceChainCustomizerConfiguration:
      Did not match:
         - @ConditionalOnEnabledResourceChain did not find class org.webjars.WebJarVersionLocator (OnEnabledResourceChainCondition)

   WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter#beanNameViewResolver:
      Did not match:
         - @ConditionalOnMissingBean (types: org.springframework.web.servlet.view.BeanNameViewResolver; SearchStrategy: all) found beans of type 'org.springframework.web.servlet.view.BeanNameViewResolver' beanNameViewResolver (OnBeanCondition)

   WebServiceTemplateAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.oxm.Marshaller' (OnClassCondition)

   WebServicesAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.ws.transport.http.MessageDispatcherServlet' (OnClassCondition)

   WebSessionIdResolverAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'reactor.core.publisher.Mono' (OnClassCondition)

   WebSocketMessagingAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer' (OnClassCondition)

   WebSocketReactiveAutoConfiguration:
      Did not match:
         - @ConditionalOnWebApplication did not find reactive web application classes (OnWebApplicationCondition)

   WebSocketServletAutoConfiguration.JettyWebSocketConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'org.eclipse.jetty.ee10.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer' (OnClassCondition)

   WebSocketServletAutoConfiguration.UndertowWebSocketConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'io.undertow.websockets.jsr.Bootstrap' (OnClassCondition)

   XADataSourceAutoConfiguration:
      Did not match:
         - @ConditionalOnClass did not find required class 'jakarta.transaction.TransactionManager' (OnClassCondition)


Exclusions:
-----------

    None


Unconditional classes:
----------------------

    org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration

    org.springframework.boot.autoconfigure.ssl.SslAutoConfiguration

    org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration

    org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration

    org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration

    org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration




java.lang.IllegalStateException: Failed to load ApplicationContext for [WebMergedContextConfiguration@6a9344f5 testClass = com.group6.defakelogibackend.DefakeLogiBackendApplicationTests, locations = [], classes = [com.group6.defakelogibackend.DefakeLogiBackendApplication], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = ["org.springframework.boot.test.context.SpringBootTestContextBootstrapper=true"], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@fba92d3, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@16a0ee18, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@0, org.springframework.boot.test.web.client.TestRestTemplateContextCustomizer@2fb3536e, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@18d87d80, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@543588e6, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@316bcf94, org.springframework.test.context.support.DynamicPropertiesContextCustomizer@0, org.springframework.boot.test.context.SpringBootTestAnnotation@a8110ec6], resourceBasePath = "src/main/webapp", contextLoader = org.springframework.boot.test.context.SpringBootContextLoader, parent = null]

	at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:180)
	at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
	at org.springframework.test.context.web.ServletTestExecutionListener.setUpRequestContextIfNecessary(ServletTestExecutionListener.java:200)
	at org.springframework.test.context.web.ServletTestExecutionListener.prepareTestInstance(ServletTestExecutionListener.java:139)
	at org.springframework.test.context.TestContextManager.prepareTestInstance(TestContextManager.java:260)
	at org.springframework.test.context.junit.jupiter.SpringExtension.postProcessTestInstance(SpringExtension.java:160)
	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
	at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
	at java.base/java.util.stream.ReferencePipeline$2$1.accept(ReferencePipeline.java:179)
	at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
	at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1708)
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
	at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:151)
	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:174)
	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:596)
	at java.base/java.util.Optional.orElseGet(Optional.java:364)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1596)
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'userController': Unsatisfied dependency expressed through field 'userService': Error creating bean with name 'userServiceImpl': Unsatisfied dependency expressed through field 'userMapper': Error creating bean with name 'userMapper' defined in file [/Users/hygok67/My-Files/Learning_Files/大三下/软件工程/DeFake/src/backend-logi/defake-logi-backend/target/classes/com/group6/defakelogibackend/mapper/UserMapper.class]: org.apache.ibatis.builder.BuilderException: Error creating document instance.  Cause: org.xml.sax.SAXParseException; lineNumber: 5; columnNumber: 117; 必须为元素类型 "mapper" 声明属性 "xmlns"。
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.resolveFieldValue(AutowiredAnnotationBeanPostProcessor.java:788)
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:768)
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:146)
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessProperties(AutowiredAnnotationBeanPostProcessor.java:509)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1445)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:600)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:339)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:347)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:337)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.instantiateSingleton(DefaultListableBeanFactory.java:1155)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingleton(DefaultListableBeanFactory.java:1121)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:1056)
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:987)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:627)
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:752)
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:439)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:318)
	at org.springframework.boot.test.context.SpringBootContextLoader.lambda$loadContext$3(SpringBootContextLoader.java:144)
	at org.springframework.util.function.ThrowingSupplier.get(ThrowingSupplier.java:58)
	at org.springframework.util.function.ThrowingSupplier.get(ThrowingSupplier.java:46)
	at org.springframework.boot.SpringApplication.withHook(SpringApplication.java:1461)
	at org.springframework.boot.test.context.SpringBootContextLoader$ContextLoaderHook.run(SpringBootContextLoader.java:563)
	at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:144)
	at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:110)
	at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContextInternal(DefaultCacheAwareContextLoaderDelegate.java:225)
	at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:152)
	... 19 more
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'userServiceImpl': Unsatisfied dependency expressed through field 'userMapper': Error creating bean with name 'userMapper' defined in file [/Users/hygok67/My-Files/Learning_Files/大三下/软件工程/DeFake/src/backend-logi/defake-logi-backend/target/classes/com/group6/defakelogibackend/mapper/UserMapper.class]: org.apache.ibatis.builder.BuilderException: Error creating document instance.  Cause: org.xml.sax.SAXParseException; lineNumber: 5; columnNumber: 117; 必须为元素类型 "mapper" 声明属性 "xmlns"。
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.resolveFieldValue(AutowiredAnnotationBeanPostProcessor.java:788)
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:768)
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:146)
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessProperties(AutowiredAnnotationBeanPostProcessor.java:509)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1445)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:600)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:339)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:347)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:337)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202)
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1667)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1555)
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.resolveFieldValue(AutowiredAnnotationBeanPostProcessor.java:785)
	... 46 more
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'userMapper' defined in file [/Users/hygok67/My-Files/Learning_Files/大三下/软件工程/DeFake/src/backend-logi/defake-logi-backend/target/classes/com/group6/defakelogibackend/mapper/UserMapper.class]: org.apache.ibatis.builder.BuilderException: Error creating document instance.  Cause: org.xml.sax.SAXParseException; lineNumber: 5; columnNumber: 117; 必须为元素类型 "mapper" 声明属性 "xmlns"。
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1812)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:601)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:339)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:347)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:337)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1609)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1555)
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.resolveFieldValue(AutowiredAnnotationBeanPostProcessor.java:785)
	... 60 more
Caused by: java.lang.IllegalArgumentException: org.apache.ibatis.builder.BuilderException: Error creating document instance.  Cause: org.xml.sax.SAXParseException; lineNumber: 5; columnNumber: 117; 必须为元素类型 "mapper" 声明属性 "xmlns"。
	at org.mybatis.spring.mapper.MapperFactoryBean.checkDaoConfig(MapperFactoryBean.java:79)
	at org.springframework.dao.support.DaoSupport.afterPropertiesSet(DaoSupport.java:44)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1859)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1808)
	... 69 more
Caused by: org.apache.ibatis.builder.BuilderException: Error creating document instance.  Cause: org.xml.sax.SAXParseException; lineNumber: 5; columnNumber: 117; 必须为元素类型 "mapper" 声明属性 "xmlns"。
	at org.apache.ibatis.parsing.XPathParser.createDocument(XPathParser.java:262)
	at org.apache.ibatis.parsing.XPathParser.<init>(XPathParser.java:127)
	at org.apache.ibatis.builder.xml.XMLMapperBuilder.<init>(XMLMapperBuilder.java:83)
	at org.apache.ibatis.builder.xml.XMLMapperBuilder.<init>(XMLMapperBuilder.java:77)
	at org.apache.ibatis.builder.annotation.MapperAnnotationBuilder.loadXmlResource(MapperAnnotationBuilder.java:163)
	at org.apache.ibatis.builder.annotation.MapperAnnotationBuilder.parse(MapperAnnotationBuilder.java:117)
	at org.apache.ibatis.binding.MapperRegistry.addMapper(MapperRegistry.java:72)
	at org.apache.ibatis.session.Configuration.addMapper(Configuration.java:943)
	at org.mybatis.spring.mapper.MapperFactoryBean.checkDaoConfig(MapperFactoryBean.java:76)
	... 72 more
Caused by: org.xml.sax.SAXParseException; lineNumber: 5; columnNumber: 117; 必须为元素类型 "mapper" 声明属性 "xmlns"。
	at java.xml/com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.createSAXParseException(ErrorHandlerWrapper.java:204)
	at java.xml/com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper.error(ErrorHandlerWrapper.java:135)
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:396)
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:327)
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLErrorReporter.reportError(XMLErrorReporter.java:284)
	at java.xml/com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator.addDTDDefaultAttrsAndValidate(XMLDTDValidator.java:1237)
	at java.xml/com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator.handleStartElement(XMLDTDValidator.java:1846)
	at java.xml/com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator.startElement(XMLDTDValidator.java:728)
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.scanStartElement(XMLDocumentFragmentScannerImpl.java:1407)
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl$ContentDriver.scanRootElementHook(XMLDocumentScannerImpl.java:1292)
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl$FragmentContentDriver.next(XMLDocumentFragmentScannerImpl.java:3079)
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl$PrologDriver.next(XMLDocumentScannerImpl.java:836)
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl.next(XMLDocumentScannerImpl.java:605)
	at java.xml/com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl.scanDocument(XMLDocumentFragmentScannerImpl.java:542)
	at java.xml/com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(XML11Configuration.java:889)
	at java.xml/com.sun.org.apache.xerces.internal.parsers.XML11Configuration.parse(XML11Configuration.java:825)
	at java.xml/com.sun.org.apache.xerces.internal.parsers.XMLParser.parse(XMLParser.java:141)
	at java.xml/com.sun.org.apache.xerces.internal.parsers.DOMParser.parse(DOMParser.java:247)
	at java.xml/com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderImpl.parse(DocumentBuilderImpl.java:342)
	at org.apache.ibatis.parsing.XPathParser.createDocument(XPathParser.java:260)
	... 80 more

 */