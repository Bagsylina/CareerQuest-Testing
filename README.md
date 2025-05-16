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
<img width="714" alt="Screenshot 2025-05-16 at 17 39 51" src="https://github.com/user-attachments/assets/c517d92f-80c5-412b-b070-6654593e23e1" />

	 \- Pentru setup-ul testelor în cadrul unei clase de test:  
import org.junit.jupiter.api.BeforeEach;  
<img width="357" alt="Screenshot 2025-05-16 at 17 39 55" src="https://github.com/user-attachments/assets/e2a1c357-1f26-468b-b1c1-a8a33ccaa842" />


- Pentru indicarea funcțiilor care constituie teste într-o clasă [\[2\]](#bookmark=id.rqkyiqsfza52)

import org.junit.jupiter.api.Test;  
<img width="716" alt="Screenshot 2025-05-16 at 17 39 59" src="https://github.com/user-attachments/assets/a07de5cb-dd35-41f1-97dd-4be599e3a6db" />


#####  	**Mockito**

Pentru mock-uirea elementelor externe claselor testate (@Mock), și **injectarea automată a dependențelor** claselor testate (@InjectMocks) [\[3\]](#bookmark=id.3whqws3cjyge):

import org.mockito.InjectMocks;  
import org.mockito.Mock;
<img width="511" alt="Screenshot 2025-05-16 at 17 40 06" src="https://github.com/user-attachments/assets/42067204-d3ad-44da-ae33-8534dab6c2a8" />

Pentru scanarea clasei testate, identificarea elementelor adnotate cu @Mock și inițializarea lor:

import org.mockito.MockitoAnnotations;
<img width="602" alt="Screenshot 2025-05-16 at 17 40 10" src="https://github.com/user-attachments/assets/06f0ac77-dc72-42cd-833c-b7ed16d6988b" />

##### **MockMvc (Spring Framework)**

Folosită în cadrul testelor pentru controller, pentru mock-uirea requesturilor la diverse endpointuri ale API-ului [\[4\]](#bookmark=id.3whqws3cjyge):  
import org.springframework.test.web.servlet.MockMvc;  
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
<img width="536" alt="Screenshot 2025-05-16 at 17 40 15" src="https://github.com/user-attachments/assets/aa46bd7e-d317-495a-81c3-a3b9fd140853" />

<img width="728" alt="Screenshot 2025-05-16 at 17 40 23" src="https://github.com/user-attachments/assets/3ec31523-6ee1-4e04-955c-98f6e195bd84" />

##### Cum am scris teste unitare

În cadrul testelor unitare, am ‘mock’-uit requesturi la elemente precum cele de mai jos, și am returnat automat răspunsuri predefinite pentru: 

- Requesturi la baza de date   
- Clasa utilitara ‘serviciu’ asociata fiecarui controller  
- Requesturi la API   
    
  În cadrul testelor pentru **controller**, am vrut sa verificam ca endpoint-urile sunt disponibile, răspund la requesturi, returnează un status corespunzător, și ‘aruncă’ mai departe excepțiile primite de la funcțiile ‘service’.  
    
  În cadrul testelor pentru **servicii**, am simulat apelurile la baza de date să returneze diverse valori predefinite, după care am apelat funcția service propriu-zisă, și am verificat prin aserții că output-ul este cel la care ne așteptam.  
    
    <img width="753" alt="Screenshot 2025-05-16 at 17 40 31" src="https://github.com/user-attachments/assets/8edf06ab-b208-4d67-b1de-0ee939aa6dfb" />



* #### Teste de integrare

  Aici nu am mai simulat dependențe externe, am folosit elementele reale pentru a vedea cum funcționează împreună.   
     
  În afară de tool-urile deja menționate, folosim:


  ##### Spring Boot Test 

  Acest tool încarcă tot contextul aplicației și permite testarea într-un mediu ‘real’

  ##### TestContainer

    
  Baza de date este deschisă în cadrul fiecărei clase de test într-un container de tip **TestContainer [\[5\]](#bookmark=id.3whqws3cjyge)**:

  import org.testcontainers.containers.MongoDBContainer;  
  
<img width="673" alt="Screenshot 2025-05-16 at 17 40 38" src="https://github.com/user-attachments/assets/a74ee219-d3f8-4f14-9a5e-8e1cbc301ee5" />


  ##### Cum am scris teste de integrare

  	Acestea au loc doar pentru controllere, și la fiecare rulare refacem containerul pentru baza de date și reintroducem date în ea pe care le folosim la rularea testelor.  
  	Apelăm diverse endpointuri, care la rândul lor apelează funcțiile service, iar apoi doar verificăm output-ul dat de funcția controller. 

	

* #### Teste End-to-end


  ##### Cucumber

  Tool-ul **Cucumber** este folosit pentru Behavior-Driven Development (BDD). Într-un fișier de tip **.feature** scris în limbajul **Gherkin,** se descriu ‘scenarii’, fiecare cu mai mulți pași.   
  Pentru fiecare pas se definește o funcție în clasa de teste. [\[6\]](#bookmark=id.3whqws3cjyge)  

<img width="600" alt="Screenshot 2025-05-16 at 17 40 43" src="https://github.com/user-attachments/assets/4b7ca7ca-3852-4b21-b2f6-ea44634aca39" />
<img width="702" alt="Screenshot 2025-05-16 at 17 40 48" src="https://github.com/user-attachments/assets/f9c18c7a-a262-48a0-bc41-3d6e443a2258" />

* #### Teste de performanta

##### 	JMeter

	  
Am folosit tool-ul **JMeter** pentru a vedea cum se comportă aplicația în anumite cazuri de requesturi, pentru a vedea ‘load’-ul asupra endpointului [\[7\]](#bookmark=id.3whqws3cjyge)

1. Am testat cazul in care se fac requesturi (nu un numar mare) de endpoint-uri cu GET (/job, /user, /employee)  
     <img width="838" alt="Screenshot 2025-05-16 at 17 40 54" src="https://github.com/user-attachments/assets/78cfb44d-d20a-4f10-b93c-f22e1fc85f99" />
     <img width="820" alt="Screenshot 2025-05-16 at 17 40 59" src="https://github.com/user-attachments/assets/43076be4-2ab0-44b7-8906-d7e654c0e7de" />

     
2. Am simulat un spike de 500 de request-uri (thread-uri) in paralel. În grafic se poate observa numărul de thread-uri active la fiecare moment

  	<img width="703" alt="Screenshot 2025-05-16 at 17 41 06" src="https://github.com/user-attachments/assets/663e44ac-47f2-40e5-89e1-7e5316f2017e" />


3. Am simulat un loop în care se creează un job, userul accesează pagina jobului noi, apoi toata lista de job-uri \- timp de un minut

	<img width="705" alt="Screenshot 2025-05-16 at 17 41 13" src="https://github.com/user-attachments/assets/8d980f06-d606-4859-80aa-12f16037219a" />


* #### Smoke Tests

	Am folosit tool-uri precum **SpringBootTest** pentru a testa că aplicația încarcă contextul bine și toate controlerele sunt instantiate corespunzător.

	De asemenea, creăm și un container pentru baza de date folosind **TestContainers** și îi setăm un url pentru conectare pentru a testa că se poate încărca pentru alte teste ulterioare.

	<img width="623" alt="Screenshot 2025-05-16 at 17 41 28" src="https://github.com/user-attachments/assets/c444b2f6-5376-47c8-81e8-a84414d1c205" />

* #### Mutanti

##### PIT (Pitest)

Am utilizat tool-ul **Pitest, versiunea 1.18.2**, care este toolul standard pentru limbajul Java pentru a crea mutanți.

Am făcut acest setup in fișierul nostru de Gradle pentru a executa mutații doar pentru Testele Unitare, folosind JUnit:  
	<img width="588" alt="Screenshot 2025-05-16 at 17 41 33" src="https://github.com/user-attachments/assets/35452b4c-5a47-4e26-a9a0-5f3d39bab28e" />


### Rezultatele testelor

#### Raport de coverage pentru **testele unitare** folosind **Jacoco** 

	<img width="631" alt="Screenshot 2025-05-16 at 17 41 41" src="https://github.com/user-attachments/assets/640d4cc8-6911-4810-989e-166d48c3d991" />

#### Rularea **testelor unitare**, de **integrare** si **smoke tests** automat prin **GitHub Actions**

	<img width="464" alt="Screenshot 2025-05-16 at 17 41 45" src="https://github.com/user-attachments/assets/e6afe6fd-d27b-40ab-8da6-eadc810dcff6" />


#### Raport **mutanti**
	<img width="640" alt="Screenshot 2025-05-16 at 17 41 55" src="https://github.com/user-attachments/assets/63aa1c17-8d6f-4e67-9ca1-dbb6d8a087e1" />


### 

### Raport AI

Am folosit ChatGPT pentru a genera teste care să acopere mai multe cazuri posibile de apelare a endpointurilor.  
	  
De exemplu, pentru fișierele următoare, am generat mai multe teste după teste deja existente scrise de noi pentru a vedea toate cazurile 

* src/test/java/ro/unibuc/careerquest/service/CVServiceTest.java

 	<img width="587" alt="Screenshot 2025-05-16 at 17 47 14" src="https://github.com/user-attachments/assets/49583ca5-c33b-48de-9682-48d8eef9a34f" />


Am folosit de asemenea AI pentru a corecta erori din testele generate anterior:  
	<img width="581" alt="Screenshot 2025-05-16 at 17 47 21" src="https://github.com/user-attachments/assets/f709507e-23ee-4ace-bb6f-210bcf3567ed" />


Rularea tuturor testelor din clasa:  
	<img width="641" alt="Screenshot 2025-05-16 at 17 47 28" src="https://github.com/user-attachments/assets/13bed7b3-f5c8-4bda-8ea2-8e683f3091e2" />


* src/test/java/ro/unibuc/careerquest/service/MatchingServiceTest.java

	Rularea tuturor testelor din clasa:  
	<img width="628" alt="Screenshot 2025-05-16 at 17 47 34" src="https://github.com/user-attachments/assets/498ed760-f6e3-4919-8dda-fbb9d4519c83" />


Testul original:
	<img width="627" alt="Screenshot 2025-05-16 at 17 47 38" src="https://github.com/user-attachments/assets/7c57112a-4266-420d-8b64-8d1cc66217e3" />

Testele generate, care ne-au ajutat sa generam multe teste într-un timp scurt, și care să abordeze situații mai complexe![][image24]
	<img width="480" alt="Screenshot 2025-05-16 at 17 47 47" src="https://github.com/user-attachments/assets/edc43c49-575f-42d7-a495-12a1896d9ec4" />

### Surse

\[1\] JUnit API \- Assert [https://junit.org/junit4/javadoc/4.13/org/junit/Assert.html](https://junit.org/junit4/javadoc/4.13/org/junit/Assert.html)  
\[2\] JUnit API \- BeforeEach [https://junit.org/junit5/docs/5.0.2/api/org/junit/jupiter/api/BeforeEach.html](https://junit.org/junit5/docs/5.0.2/api/org/junit/jupiter/api/BeforeEach.html)  
\[3\] Mockito \- Inject Mocks [https://site.mockito.org/javadoc/current/org/mockito/InjectMocks.html](https://site.mockito.org/javadoc/current/org/mockito/InjectMocks.html)  
\[4\] MockMvc [https://docs.spring.io/spring-framework/reference/testing/mockmvc.html](https://docs.spring.io/spring-framework/reference/testing/mockmvc.html)  
\[5\] TestContainers for Java and MongoDB [https://java.testcontainers.org/modules/databases/mongodb/](https://java.testcontainers.org/modules/databases/mongodb/)  
\[6\] Cucumber Tutorial [https://cucumber.io/docs/guides/10-minute-tutorial/](https://cucumber.io/docs/guides/10-minute-tutorial/)  
\[7\] JMeter Apache [https://jmeter.apache.org/usermanual/build-web-test-plan.html](https://jmeter.apache.org/usermanual/build-web-test-plan.html)  
\[8\] OpenAI, ChatGPT [https://chatgpt.com/share/6825f09e-571c-8008-b38f-9d3955885322](https://chatgpt.com/share/6825f09e-571c-8008-b38f-9d3955885322) Data generarii: 16 mai 2025

 
