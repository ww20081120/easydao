/**************************************************************************************** 
 Copyright © 2003-2012 ZTEsoft Corporation. All rights reserved. Reproduction or       <br>
 transmission in whole or in part, in any form or by any means, electronic, mechanical <br>
 or otherwise, is prohibited without the prior written consent of the copyright owner. <br>
 ****************************************************************************************/
package example.easydao.model;

import java.io.Serializable;

/**
 * <Description> <br>
 * 
 * @author 王伟 <br>
 * @version 1.0 <br>
 * @CreateDate 2014年10月28日 <br>
 * @see example.easydao.model <br>
 */
public class Salgrade implements Serializable {

    /**
     * serialVersionUID <br>
     */
    private static final long serialVersionUID = 874500062940086497L;

    private int grade;

    private double losal;

    private double hisal;

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public double getLosal() {
        return losal;
    }

    public void setLosal(double losal) {
        this.losal = losal;
    }

    public double getHisal() {
        return hisal;
    }

    public void setHisal(double hisal) {
        this.hisal = hisal;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Salgrade [grade=" + grade + ", losal=" + losal + ", hisal=" + hisal + "]";
    }
}
