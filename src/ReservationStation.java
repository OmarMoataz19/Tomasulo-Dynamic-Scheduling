public class ReservationStation {
    private ReservationSlot[] reservationStation;

    public ReservationStation(int size , String type ) {
        this.reservationStation = new ReservationSlot[size];
        switch(type){
            case "add":
                for (int i = 0; i < size; i++) {
                    this.reservationStation[i] = new ReservationSlot("A" + i);
                }
                break;
            case "mult":
                for (int i = 0; i < size; i++) {
                    this.reservationStation[i] = new ReservationSlot("M" + i);
                }
                break;
        }
    }

    public ReservationSlot[] getReservationStation() {
        return reservationStation;
    }

    public int getSize() {
        return this.reservationStation.length;
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < this.reservationStation.length; i++) {
            result += this.reservationStation[i].getName() + ": " + this.reservationStation[i].getOp() + " , Vj: " + this.reservationStation[i].getVj() + " , Vk: " + this.reservationStation[i].getVk() + " , Qj: " + this.reservationStation[i].getQj() + " , Qk: " + this.reservationStation[i].getQk() + " , A: " + this.reservationStation[i].getA() + " , Busy: " + this.reservationStation[i].isBusy() + " , Ready: " + this.reservationStation[i].isReady() +" , Time: "+ this.reservationStation[i].getTimer() + " \n";
        }
        return result;
    }

    public void setReservationStation(ReservationSlot[] reservationStation) {
        this.reservationStation = reservationStation;
    }

    public void setReservationSlot(int index, ReservationSlot reservationSlot) {
        this.reservationStation[index] = reservationSlot;
    }

    public ReservationSlot getReservationSlot(int index) {
        return this.reservationStation[index];
    }

    public void clearReservationSlot(int index){
        this.reservationStation[index].setBusy(false);
        this.reservationStation[index].setReady(true);
        this.reservationStation[index].setOp("");
        this.reservationStation[index].setVj(0);
        this.reservationStation[index].setVk(0);
        this.reservationStation[index].setQj("0");
        this.reservationStation[index].setQk("0");
        this.reservationStation[index].setA(0);
    }
    public int getFreeReservationSlot(){
        for(int i=0; i<this.reservationStation.length;i++){
            if(this.reservationStation[i].isBusy()==false){
                return i;
        }
    }
        return -1;
    }
}
