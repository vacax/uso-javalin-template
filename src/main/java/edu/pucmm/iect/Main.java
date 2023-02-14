package edu.pucmm.iect;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinFreemarker;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.rendering.template.JavalinVelocity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {
        var app = Javalin.create(javalinConfig -> {
                    javalinConfig.staticFiles.add("/publico", Location.CLASSPATH);
                })
                .start(7070);

        //Endpoint
        app.get("/", ctx -> ctx.result("Ejemplo de Javalin con Plantillas de Renderizado"));

        //Registrando los templates. es necesario incluir la libería io.javalin:javalin-rendering:5.3.2
        JavalinRenderer.register(new JavalinFreemarker(), ".ftl");
        JavalinRenderer.register(new JavalinThymeleaf(), ".html");
        JavalinRenderer.register(new JavalinVelocity(), ".vm");


        app.routes(() -> {


            /**
             * Cada sistema de plantilla incluye etiquetas y tiene su forma de trabajo:
             * ir a https://freemarker.apache.org/docs/dgui.html
             */
            path("/freemarker", () -> {

                /**
                 * Validando el sistema de plantilla
                 * Ir a: http://localhost:7070/freemarker/datosEstudiante/20011136
                 */
                get("/datosEstudiante/{matricula}", ctx -> {
                    //tomando el parametro utl y validando el tipo.
                    int matricula = ctx.pathParamAsClass("matricula", Integer.class).get();
                    Estudiante estudiante = new Estudiante(matricula, "Estudiante matricula: "+matricula, "ISC");
                    //
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("estudiante", estudiante);
                    //enviando al sistema de plantilla.
                    ctx.render("/templates/freemarker/datosEstudiante.ftl", modelo);
                });
            });

            /**
             * Cada sistema de plantilla incluye etiquetas y tiene su forma de trabajo:
             * ir a https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
             */
            app.routes( ()-> {
                path("/thymeleaf", () -> {

                            /**
                             * http://localhost:7070/thymeleaf/
                             */
                            get("/", ctx -> {
                                List<Estudiante> listaEstudiante = getEstudiantes();

                                Map<String, Object> modelo = new HashMap<>();
                                modelo.put("titulo", "Ejemplo de funcionalidad Thymeleaf");
                                modelo.put("listaEstudiante", listaEstudiante);

                                //
                                ctx.render("/templates/thymeleaf/funcionalidad.html", modelo);
                            });
                        });

                /**
                 * Cada sistema de plantilla incluye etiquetas y tiene su forma de trabajo:
                 * ir a https://velocity.apache.org/engine/2.2/user-guide.html
                 */
                path("/velocity", () -> {

                    /**
                     * http://localhost:7070/velocity/
                     */
                    get("/", ctx -> {
                        //listando los estudiantes..
                        List<Estudiante> listaEstudiante = getEstudiantes();

                        Map<String, Object> modelo = new HashMap<>();
                        modelo.put("titulo", "Ejemplo de funcionalidad Velocity");
                        modelo.put("listaEstudiante", listaEstudiante);

                        //
                        ctx.render("/templates/velocity/funcionalidad.vm", modelo);
                    });
                });



            });



        });
    }

    /**
     *
     */
    public static class Estudiante{
        int matricula;
        String nombre;
        String carrera;

        public Estudiante() {
        }

        public Estudiante(int matricula, String nombre, String carrera) {
            this.matricula = matricula;
            this.nombre = nombre;
            this.carrera = carrera;
        }

        public int getMatricula() {
            return matricula;
        }

        public void setMatricula(int matricula) {
            this.matricula = matricula;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCarrera() {
            return carrera;
        }

        public void setCarrera(String carrera) {
            this.carrera = carrera;
        }
    }

    /**
     *
     * @return
     */
    private static List<Estudiante> getEstudiantes() {
        //listando los estudiantes..
        List<Estudiante> listaEstudiante = new ArrayList<>();
        listaEstudiante.add(new Estudiante(20011136, "Carlos Camacho", "ITT"));
        listaEstudiante.add(new Estudiante(20011137, "Otro Estudiante", "ISC"));
        listaEstudiante.add(new Estudiante(20011138, "Otro otro", "ISC"));
        return listaEstudiante;
    }


}