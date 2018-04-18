/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.controller;

import javafx.stage.Stage;

/**
 *
 * @author jcalvo
 */
public abstract class Controller {

   private String accion;
   private Stage stage;

   public String getAccion() {
      return accion;
   }

   public void setAccion(String accion) {
      this.accion = accion;
   }

   public Stage getStage() {
      return stage;
   }

   public void setStage(Stage stage) {
      this.stage = stage;
   }   
   
   public abstract void initialize();
}
