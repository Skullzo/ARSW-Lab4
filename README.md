# Escuela Colombiana de Ingeniería
# Arquitecturas de Software - ARSW
## Taller – Principio de Inversión de dependencias, Contenedores Livianos e Inyección de dependencias.
### Parte I. Ejercicio básico.

Para ilustrar el uso del framework Spring, y el ambiente de desarrollo para el uso del mismo a través de Maven (y NetBeans), se hará la configuración de una aplicación de análisis de textos, que hace uso de un verificador gramatical que requiere de un corrector ortográfico. A dicho verificador gramatical se le inyectará, en tiempo de ejecución, el corrector ortográfico que se requiera (por ahora, hay dos disponibles: inglés y español).

1. Abra el los fuentes del proyecto en NetBeans.

2. Revise el archivo de configuración de Spring ya incluido en el proyecto (src/main/resources). El mismo indica que Spring buscará automáticamente los 'Beans' disponibles en el paquete indicado.

3. Haciendo uso de la [configuración de Spring basada en anotaciones](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-spring-beans-and-dependency-injection.html) marque con las anotaciones @Autowired y @Service las dependencias que deben inyectarse, y los 'beans' candidatos a ser inyectadas -respectivamente-:

	* GrammarChecker será un bean, que tiene como dependencia algo de tipo 'SpellChecker'.
	
	**Para que GrammarChecker sea un bean, que tiene como dependencia algo de tipo 'SpellChecker', se agrega a la clase ```GrammarChecker``` la anotación ```@Service```, y en ```SpellChecker``` se agrega la anotación ```@Autowired```, quedando la clase ```GrammarChecker``` como se ve a continuación.**
	
	```java
	@Service
	public class GrammarChecker {	
		@Autowired
		SpellChecker sc;
		String x;            
		public SpellChecker getSpellChecker() {
			return sc;
		}
		public void setSpellChecker(SpellChecker sc) {
			this.sc = sc;
		}
		public String check(String text){		
			StringBuffer sb=new StringBuffer();
			sb.append("Spell checking output:"+sc.checkSpell(text));
			sb.append("Plagiarism checking output: Not available yet");	
			return sb.toString();	
		}	
	}
	```
	
	* EnglishSpellChecker y SpanishSpellChecker son los dos posibles candidatos a ser inyectados. Se debe seleccionar uno, u otro, mas NO ambos (habría conflicto de resolución de dependencias). Por ahora haga que se use EnglishSpellChecker.
	
	**Para hacer que se use ```EnglishSpellChecker```, se agrega la anotación ```@Service``` a la clase ```EnglishSpellChecker```, quedando el código de la siguiente forma.**
	
	```java
	@Service
	public class EnglishSpellChecker implements SpellChecker {
		@Override
		public String checkSpell(String text) {		
			return "Checked with english checker:"+text;
		}      
	}
	```
 
5.	Haga un programa de prueba, donde se cree una instancia de GrammarChecker mediante Spring, y se haga uso de la misma:

	```java
	public static void main(String[] args) {
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		GrammarChecker gc=ac.getBean(GrammarChecker.class);
		System.out.println(gc.check("la la la "));
	}
	```
	
	**Para realizar un programa de prueba, se crean dos instancias en ```GrammarChecker``` mediante Spring, en donde en la clase ```Main``` se implementan dos nuevos métodos llamados ```PrimeraPrueba``` y ```SegundaPrueba``` respectivamente. Cada una de ellas realizan dos pruebas por separado dodne realiza la ejecución del programa con la frase ingresada en inglés en cada una de ellas, como se observa en el código a continuación.**
	
	```java
	public class Main {
	    	public static void main(String a[]) {
			PrimeraPrueba();
			SegundaPrueba();
	    	}
	    	public static void PrimeraPrueba(){
			ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
			GrammarChecker gc = ac.getBean(GrammarChecker.class);
			System.out.println(gc.check("Performing grammar check in Spanish. "));
	    	}	
	    	public static void SegundaPrueba(){
			ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
			GrammarChecker gc = ac.getBean(GrammarChecker.class);
			System.out.println(gc.check("Performing second grammar check in Spanish. "));
	    	}	
	}
	```
	
	**Luego de realizar la implementación de las dos pruebas, al compilar y ejecutar en Maven, se obtiene el siguiente resultado. Como se puede ver a continuación, se realiza la respectiva ejecución de ```EnglishSpellChecker``` como se propuso en el enunciado.**
	
	```
	Spell checking output:Checked with english checker:Performing grammar check in Spanish. Plagiarism checking output: Not available yet
	Spell checking output:Checked with english checker:Performing second grammar check in Spanish. Plagiarism checking output: Not available yet
	```
	
6.	Modifique la configuración con anotaciones para que el Bean ‘GrammarChecker‘ ahora haga uso del  la clase SpanishSpellChecker (para que a GrammarChecker se le inyecte EnglishSpellChecker en lugar de  SpanishSpellChecker. Verifique el nuevo resultado.

**Ahora para que el Bean ```GrammarChecker``` ahora haga uso del  la clase ```SpanishSpellChecker```, primero en la clase ```EnglishSpellChecker``` se quita la anotación ```@Service```, y ésta misma anotación se agrega a la clase ```SpanishSpellChecker```, quedando así la clase ```SpanishSpellChecker``` de la siguiente forma.**

```java
@Service
public class SpanishSpellChecker implements SpellChecker {
	@Override
	public String checkSpell(String text) {
		return "revisando ("+text+") con el verificador de sintaxis del espanol";                            
	}
}
```

**Luego en la clase ```Main```, cambiamos ahora el texto ingresado en las dos pruebas de inglés a español, para realizar la respectiva verificación de gramática en español, quedando de la siguiente forma.**

```java
public class Main {
    public static void main(String a[]) {
    	PrimeraPrueba();
        SegundaPrueba();
    }  
    public static void PrimeraPrueba(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        GrammarChecker gc = ac.getBean(GrammarChecker.class);
        System.out.println(gc.check("Realizando verificación de gramática en español. "));
    }
    public static void SegundaPrueba(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
        GrammarChecker gc = ac.getBean(GrammarChecker.class);
        System.out.println(gc.check("Realizando segunda verificación de gramática en español. "));
    }
}
```

**Luego de compilar y ejecutar en Maven tras hacer las respectivas modificaciones a las pruebas, se obtiene el siguiente resultado. Como se puede ver a continuación, se realiza la respectiva ejecución de ```SpanishSpellChecker``` como se propuso en el enunciado.**

```
Spell checking output:revisando (Realizando verificación de gramática en español. ) con el verificador de sintaxis del espanolPlagiarism checking output: Not available yet
Spell checking output:revisando (Realizando segunda verificación de gramática en español. ) con el verificador de sintaxis del espanolPlagiarism checking output: Not available yet
```

----------

## Laboratorio Componentes y conectores  Middleware - gestión de planos

### Dependencias
* [Ejercicio introductorio al manejo de Spring y la configuración basada en anotaciones](https://github.com/ARSW-ECI-beta/DIP_DI-SPRING_JAVA-GRAMMAR_CHECKER).

### Descripción
En este ejercicio se va a construír un modelo de clases para la capa lógica de una aplicación que permita gestionar planos arquitectónicos de una prestigiosa compañia de diseño. 

![](img/ClassDiagram1.png)

### Parte II.

1. Configure la aplicación para que funcione bajo un esquema de inyección de dependencias, tal como se muestra en el diagrama anterior.


	Lo anterior requiere:

	* Agregar las dependencias de Spring.
	* Agregar la configuración de Spring.
	* Configurar la aplicación -mediante anotaciones- para que el esquema de persistencia sea inyectado al momento de ser creado el bean 'BlueprintServices'.


2. Complete los operaciones getBluePrint() y getBlueprintsByAuthor(). Implemente todo lo requerido de las capas inferiores (por ahora, el esquema de persistencia disponible 'InMemoryBlueprintPersistence') agregando las pruebas correspondientes en 'InMemoryPersistenceTest'.

**Para realizar el siguiente procedimiento, en la clase ```InMemoryBlueprintPersistence``` completamos las operaciones correspondientes de ```getBluePrint()```, quedando de la siguiente forma.**

```java
@Override
public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
	return blueprints.get(new Tuple<>(author, bprintname));
}
```

**Luego, en la clase ```BlueprintsPersistence``` se completan las respectivas operaciones de ```getBluePrint()``` de la siguiente forma.**

```java
public Blueprint getBlueprint(String author,String name) throws BlueprintNotFoundException{
	return bpp.getBlueprint(author, name);
}
```

**Ahora en la clase ```InMemoryBlueprintPersistence``` se completan las operaciones de ```getBlueprintByAuthor()``` de la siguiente forma.**

```java
public Set<Blueprint> getBlueprintByAuthor(String auth) {
      Set<Blueprint> authorBlueprints = new HashSet<>();
      for (Tuple<String, String> key : blueprints.keySet()) {
          if (key.getElem1().equals(auth)) {
              authorBlueprints.add(blueprints.get(key));
          }
      }
      return authorBlueprints;
}
```

**Para completar las operaciones de ```getBlueprintsByAuthor()``` en la clase ```BlueprintsPersistence```, se agregan las siguientes modificaciones.**

```java
public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
     return bpp.getBlueprintByAuthor(author);
}
```

**Para implementar todo lo requerido de las capas inferiores, se agregan las siguientes pruebas correspondientes en ```InMemoryPersistenceTest``` tanto para ```getBluePrint()```, como para ```getBlueprintsByAuthor()``` respectivamente, quedando de la siguiente forma.**

```java
@Test
  public void getBlueprintTest(){
      InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();
      Point[] pts = new Point[]{new Point(0, 0),new Point(10, 10)};
      Blueprint bp = new Blueprint("john", "theRipper",pts);
      try {
          ibpp.saveBlueprint(bp);
      } catch (BlueprintPersistenceException e) {
          fail("InMemoryBluePrintsPersistence save error.");
      }
      Blueprint resultBp=null;
      try {
          resultBp = ibpp.getBlueprint("john","theRipper");
      } catch (BlueprintNotFoundException e) {
          fail("InMemoryBluePrintsPersistence get error.");
      }
      assertEquals(resultBp,bp);
  }
@Test
  public void getBlueprintByAuthorTest(){
      InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();
      Point[] pts1 = new Point[]{new Point(0, 0),new Point(10, 10)};
      Blueprint bp1 = new Blueprint("john", "thepaint",pts1);
      Point[] pts2 = new Point[]{new Point(0, 45),new Point(45, 10)};
      Blueprint bp2 = new Blueprint("john", "theRipper",pts2);
      Point[] pts3 = new Point[]{new Point(23, 43),new Point(56, 10)};
      Blueprint bp3 = new Blueprint("juan", "saltaMuros",pts3);
      Set<Blueprint> authorBlueprints = new HashSet<>();
      authorBlueprints.add(bp1);
      authorBlueprints.add(bp2);

      try {
          ibpp.saveBlueprint(bp1);
          ibpp.saveBlueprint(bp2);
          ibpp.saveBlueprint(bp3);
      } catch (BlueprintPersistenceException ex) {
          fail("InMemoryBluePrintsPersistence save error.");
      }
      assertEquals(ibpp.getBlueprintByAuthor("john"),authorBlueprints);
  }
}
```

3. Haga un programa en el que cree (mediante Spring) una instancia de BlueprintServices, y rectifique la funcionalidad del mismo: registrar planos, consultar planos, registrar planos específicos, etc.

**Para realizar el siguiente procedimiento, primero se crea en la clase ```InMemoryBlueprintPersistence``` el método ```updateBlueprint```, para actualizar los planos en cuestión. Se implementó una tupla que contiene parámetros ```auth``` y ```name```, y en caso de que no exista el plano, arroja una excepción de que el plano dado no existe.**

```java
@Override
public void updateBlueprint(Blueprint bp, String auth, String name) throws BlueprintPersistenceException {
       if (blueprints.containsKey(new Tuple<>(bp.getAuthor(), bp.getName()))) {
           blueprints.remove(new Tuple<>(auth, name));
           blueprints.put(new Tuple<>(auth, name), bp);
       } else {
           throw new BlueprintPersistenceException("The given blueprint does not exists: " + bp);
       }
}
```

**Ahora, desde la clase ```BlueprintService``` creamos también el método ```updateBlueprint```, para poder realizar la respectiva actualización de los planos en cuestión.**

```java
public void updateBlueprint(Blueprint bp, String auth,String name) throws BlueprintPersistenceException {
       bpp.updateBlueprint(bp,auth,name);
}
```

4. Se quiere que las operaciones de consulta de planos realicen un proceso de filtrado, antes de retornar los planos consultados. Dichos filtros lo que buscan es reducir el tamaño de los planos, removiendo datos redundantes o simplemente submuestrando, antes de retornarlos. Ajuste la aplicación (agregando las abstracciones e implementaciones que considere) para que a la clase BlueprintServices se le inyecte uno de dos posibles 'filtros' (o eventuales futuros filtros). No se contempla el uso de más de uno a la vez:
	* (A) Filtrado de redundancias: suprime del plano los puntos consecutivos que sean repetidos.
	* (B) Filtrado de submuestreo: suprime 1 de cada 2 puntos del plano, de manera intercalada.

5. Agrege las pruebas correspondientes a cada uno de estos filtros, y pruebe su funcionamiento en el programa de prueba, comprobando que sólo cambiando la posición de las anotaciones -sin cambiar nada más-, el programa retorne los planos filtrados de la manera (A) o de la manera (B). 

## Autores
[Alejandro Toro Daza](https://github.com/Skullzo)

[David Fernando Rivera Vargas](https://github.com/DavidRiveraRvD)
## Licencia & Derechos de Autor
**©** Alejandro Toro Daza, David Fernando Rivera Vargas. Estudiantes de Ingeniería de Sistemas de la [Escuela Colombiana de Ingeniería Julio Garavito](https://www.escuelaing.edu.co/es/).

Licencia bajo la [GNU General Public License](https://github.com/Skullzo/ARSW-Lab4/blob/main/LICENSE).
