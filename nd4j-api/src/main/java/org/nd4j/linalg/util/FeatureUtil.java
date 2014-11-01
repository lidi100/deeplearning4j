package org.nd4j.linalg.util;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Feature matrix related utils
 */
public class FeatureUtil {
    /**
     * Creates an out come vector from the specified inputs
     * @param index the index of the label
     * @param numOutcomes the number of possible outcomes
     * @return a binary label matrix used for supervised learning
     */
    public static INDArray toOutcomeVector(int index,int numOutcomes) {
        int[] nums = new int[numOutcomes];
        nums[index] = 1;
        return ArrayUtil.toNDArray(nums);
    }



    /**
     * Creates an out come vector from the specified inputs
     * @param index the index of the label
     * @param numOutcomes the number of possible outcomes
     * @return a binary label matrix used for supervised learning
     */
    public static INDArray toOutcomeMatrix(int[] index,int numOutcomes) {
        INDArray ret = Nd4j.create(index.length, numOutcomes);
        for(int i = 0; i < ret.rows(); i++) {
            int[] nums = new int[numOutcomes];
            nums[index[i]] = 1;
            ret.putRow(i, ArrayUtil.toNDArray(nums));
        }

        return ret;
    }

    public static  void normalizeMatrix(INDArray toNormalize) {
        INDArray columnMeans = toNormalize.mean(0);
        toNormalize.subiRowVector(columnMeans);
        INDArray std = toNormalize.std(0);
        std.addi(Nd4j.scalar(1e-12));
        toNormalize.diviRowVector(std);
    }

    /**
     * Divides each row by its max
     *
     * @param toScale the matrix to divide by its row maxes
     */
    public static void scaleByMax(INDArray toScale) {
        INDArray scale = toScale.max(1);
        for (int i = 0; i < toScale.rows(); i++) {
            double scaleBy = scale.getDouble(i);
            toScale.putRow(i, toScale.getRow(i).divi(scaleBy));
        }
    }


    /**
     * Scales the ndarray columns
     * to the given min/max values
     * @param min the minimum number
     * @param max the max number
     */
    public static void scaleMinMax(double min,double max,INDArray toScale) {
        //X_std = (X - X.min(axis=0)) / (X.max(axis=0) - X.min(axis=0)) X_scaled = X_std * (max - min) + min

        INDArray min2 = toScale.min(0).broadcast(toScale.shape()).transpose();
        INDArray max2 = toScale.max(0).broadcast(toScale.shape()).transpose();

        INDArray std = toScale.sub(min2).divi(max2.sub(min2));

        INDArray scaled = std.mul(max - min).addi(min);
        toScale.assign(scaled);
    }



}
