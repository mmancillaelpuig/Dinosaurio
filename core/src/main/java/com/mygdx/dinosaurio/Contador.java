    package com.mygdx.dinosaurio;

    public class Contador {

        private float tiempo;
        private int metros;


        public Contador() {
            this.tiempo = 0;
            this.metros = 0;
        }

        public float getTiempo() {
            return tiempo;
        }

        public void setTiempo(float tiempo) {
            this.tiempo = tiempo;
        }

        public int getMetros() {
            return metros;
        }

        public void setMetros(int metros) {
            this.metros = metros;
        }


        public boolean update(float delta) {
            tiempo += delta;
            if (tiempo >= 1f) {
                metros += 5;
                tiempo -= 1f;
                return true;
            }
            return false;
        }



        public void reset(){
            this.metros = 0;
            this.tiempo = 0;
        }
    }
