### Descrierea aplicației 	

Aplicația noastra se numește ‘CareerQuest’ si este dezvoltată în **Java** folosind **Spring Boot,** un framework  fiind partea de ‘backend’ al unei platforme de găsit joburi - unde angajatorii pot posta joburi, si selecta dintre aplicanți, iar userii își pot crea CV-uri, căuta și aplica la joburi, și pot primi de asemenea recomandări de locuri de muncă potrivite lor.  
	

### Diagrame

### Tipuri de teste, tool-uri folosite și configurarea lor

* #### Teste unitare

    
   Am scris teste unitare pentru a testa că funcționează corect atât controllerele, cât și funcțiile ‘service’ apelate de controllere. 

  Pentru testele unitare am folosit:

##### 	**JUnit** 

Folosit pentru declararea părților de test, cum ar fi:  
	 Pentru asertii: [**\[1\]**](#bookmark=id.lnrzq9nss4rq)  
	 \- import static org.junit.Assert.assertTrue;  
	 \- import static org.junit.jupiter.api.Assertions.assertThrows;  

	 \- Pentru setup-ul testelor în cadrul unei clase de test:  
import org.junit.jupiter.api.BeforeEach;  
![][image3]

- Pentru indicarea funcțiilor care constituie teste într-o clasă [\[2\]](#bookmark=id.rqkyiqsfza52)

import org.junit.jupiter.api.Test;  
![][image4]

#####  	**Mockito**

Pentru mock-uirea elementelor externe claselor testate (@Mock), și **injectarea automată a dependențelor** claselor testate (@InjectMocks) [\[3\]](#bookmark=id.3whqws3cjyge):

import org.mockito.InjectMocks;  
import org.mockito.Mock;

Pentru scanarea clasei testate, identificarea elementelor adnotate cu @Mock și inițializarea lor:

import org.mockito.MockitoAnnotations;

##### **MockMvc (Spring Framework)**

Folosită în cadrul testelor pentru controller, pentru mock-uirea requesturilor la diverse endpointuri ale API-ului [\[4\]](#bookmark=id.3whqws3cjyge):  
import org.springframework.test.web.servlet.MockMvc;  
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

![][image5]  
![][image6]

##### Cum am scris teste unitare

În cadrul testelor unitare, am ‘mock’-uit requesturi la elemente precum cele de mai jos, și am returnat automat răspunsuri predefinite pentru: 

- Requesturi la baza de date   
- Clasa utilitara ‘serviciu’ asociata fiecarui controller  
- Requesturi la API   
    
  În cadrul testelor pentru **controller**, am vrut sa verificam ca endpoint-urile sunt disponibile, răspund la requesturi, returnează un status corespunzător, și ‘aruncă’ mai departe excepțiile primite de la funcțiile ‘service’.  
    
  În cadrul testelor pentru **servicii**, am simulat apelurile la baza de date să returneze diverse valori predefinite, după care am apelat funcția service propriu-zisă, și am verificat prin aserții că output-ul este cel la care ne așteptam.  
    
    
  ![][image7]

* #### Teste de integrare

  Aici nu am mai simulat dependențe externe, am folosit elementele reale pentru a vedea cum funcționează împreună.   
     
  În afară de tool-urile deja menționate, folosim:


  ##### Spring Boot Test 

  Acest tool încarcă tot contextul aplicației și permite testarea într-un mediu ‘real’

  ##### TestContainer

    
  Baza de date este deschisă în cadrul fiecărei clase de test într-un container de tip **TestContainer [\[5\]](#bookmark=id.3whqws3cjyge)**:

  import org.testcontainers.containers.MongoDBContainer;  
  

  ![][image8]


  ##### Cum am scris teste de integrare

  	Acestea au loc doar pentru controllere, și la fiecare rulare refacem containerul pentru baza de date și reintroducem date în ea pe care le folosim la rularea testelor.  
  	Apelăm diverse endpointuri, care la rândul lor apelează funcțiile service, iar apoi doar verificăm output-ul dat de funcția controller. 

	

* #### Teste End-to-end


  ##### Cucumber

  Tool-ul **Cucumber** este folosit pentru Behavior-Driven Development (BDD). Într-un fișier de tip **.feature** scris în limbajul **Gherkin,** se descriu ‘scenarii’, fiecare cu mai mulți pași.   
  Pentru fiecare pas se definește o funcție în clasa de teste. [\[6\]](#bookmark=id.3whqws3cjyge)  
  ![][image9]  
  ![][image10]

* #### Teste de performanta

##### 	JMeter

	  
Am folosit tool-ul **JMeter** pentru a vedea cum se comportă aplicația în anumite cazuri de requesturi, pentru a vedea ‘load’-ul asupra endpointului [\[7\]](#bookmark=id.3whqws3cjyge)

1. Am testat cazul in care se fac requesturi (nu un numar mare) de endpoint-uri cu GET (/job, /user, /employee)  
     
     
2. Am simulat un spike de 500 de request-uri (thread-uri) in paralel. În grafic se poate observa numărul de thread-uri active la fiecare moment

   ![][image11]

   ![][image12]

   ![][image13]

3. Am simulat un loop în care se creează un job, userul accesează pagina jobului noi, apoi toata lista de job-uri \- timp de un minut

![][image14]  
![][image15]

* #### Smoke Tests

	Am folosit tool-uri precum **SpringBootTest** pentru a testa că aplicația încarcă contextul bine și toate controlerele sunt instantiate corespunzător.

	De asemenea, creăm și un container pentru baza de date folosind **TestContainers** și îi setăm un url pentru conectare pentru a testa că se poate încărca pentru alte teste ulterioare.

* #### Mutanti

##### PIT (Pitest)

Am utilizat tool-ul **Pitest, versiunea 1.18.2**, care este toolul standard pentru limbajul Java pentru a crea mutanți.

Am făcut acest setup in fișierul nostru de Gradle pentru a executa mutații doar pentru Testele Unitare, folosind JUnit:  
![][image16]

### Rezultatele testelor

#### Raport de coverage pentru **testele unitare** folosind **Jacoco** ![][image17] 

#### 

#### 

#### Rularea **testelor unitare**, de **integrare** si **smoke tests** automat prin **GitHub Actions**

![][image18]

#### Raport **mutanti**

![][image19]

### 

### Raport AI

	Am folosit ChatGPT pentru a genera teste care să acopere mai multe cazuri posibile de apelare a endpointurilor.  
	  
	De exemplu, pentru fișierele următoare, am generat mai multe teste după teste deja existente scrise de noi pentru a vedea toate cazurile [\[8\]](#bookmark=id.5ud0z9ngchp1)

* src/test/java/ro/unibuc/careerquest/service/CVServiceTest.java

![][image20]

Am folosit de asemenea AI pentru a corecta erori din testele generate anterior:  
![][image21]

Rularea tuturor testelor din clasa:  
![][image22]

* src/test/java/ro/unibuc/careerquest/service/MatchingServiceTest.java

	Rularea tuturor testelor din clasa:  
![][image23]

Testul original:

Testele generate, care ne-au ajutat sa generam multe teste într-un timp scurt, și care să abordeze situații mai complexe![][image24]

### Surse

\[1\] JUnit API \- Assert [https://junit.org/junit4/javadoc/4.13/org/junit/Assert.html](https://junit.org/junit4/javadoc/4.13/org/junit/Assert.html)  
\[2\] JUnit API \- BeforeEach [https://junit.org/junit5/docs/5.0.2/api/org/junit/jupiter/api/BeforeEach.html](https://junit.org/junit5/docs/5.0.2/api/org/junit/jupiter/api/BeforeEach.html)  
\[3\] Mockito \- Inject Mocks [https://site.mockito.org/javadoc/current/org/mockito/InjectMocks.html](https://site.mockito.org/javadoc/current/org/mockito/InjectMocks.html)  
\[4\] MockMvc [https://docs.spring.io/spring-framework/reference/testing/mockmvc.html](https://docs.spring.io/spring-framework/reference/testing/mockmvc.html)  
\[5\] TestContainers for Java and MongoDB [https://java.testcontainers.org/modules/databases/mongodb/](https://java.testcontainers.org/modules/databases/mongodb/)  
\[6\] Cucumber Tutorial [https://cucumber.io/docs/guides/10-minute-tutorial/](https://cucumber.io/docs/guides/10-minute-tutorial/)  
\[7\] JMeter Apache [https://jmeter.apache.org/usermanual/build-web-test-plan.html](https://jmeter.apache.org/usermanual/build-web-test-plan.html)  
\[8\] OpenAI, ChatGPT [https://chatgpt.com/share/6825f09e-571c-8008-b38f-9d3955885322](https://chatgpt.com/share/6825f09e-571c-8008-b38f-9d3955885322) Data generarii: 16 mai 2025

 
