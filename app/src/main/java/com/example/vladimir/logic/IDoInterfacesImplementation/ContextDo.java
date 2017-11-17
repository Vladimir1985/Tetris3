package com.example.vladimir.logic.IDoInterfacesImplementation;

import com.example.vladimir.struct.CurrentState;

public class  ContextDo{
    private IDo iDo;

    public ContextDo() {
    }

    public void setStrategy(IDo iDo) {
        this.iDo = iDo;
    }

    public CurrentState DoStrategy(CurrentState state){
        return iDo.Do(state);
    }
}
