package timeseries_classification;

import utilities.ClassifierTools;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;

public class TrainAndPredict {

    public Object train(String[] args) throws Exception {
        String request =args[0];
        String[] params=Arrays.copyOfRange(args,1,args.length);

        HandleChain train = new Train();
        HandleChain trainWithHyper = new TrainWithHyper();
        HandleChain noHandle = new NoHandle();
        train.setNext(trainWithHyper);
        trainWithHyper.setNext(noHandle);
        Object o =train.process(request,params);
        return o;
    }

    public Object predict(String[] args) throws Exception {
        String request =args[0];
        String[] params=Arrays.copyOfRange(args,1,args.length);
        HandleChain predict = new Predict();
        HandleChain noHandle = new NoHandle();
        predict.setNext(noHandle);
        Object o = predict.process(request,params);
        return o;
    }

//    /**
//     *
//     * @param params
//     * params[0]: the address of data
//     * params[1]: the address to store the module
//     * params[2]: the name of classifiers
//     * params[3]: 0- train without cross validation, 1-train with cross validation
//     * @return the builded classifiers and write to
//     */
//    public Object train(String[] params) throws IOException {
//        String dataAddress=params[0];
//        String moduleAddress=params[1];
//        String classiferName=params[2];
//        String cvFlag = params[3];
//        Instances data = ClassifierTools.loadData(dataAddress);  // training data
//        Object c=null;
//        try {
//            Class algorithm=Class.forName(classiferName);  // reflection out the class
//            c=algorithm.newInstance();
//
//            Method builderClassifier=algorithm.getMethod("buildClassifier",data.getClass());
//            // buidClassifier is a method shared across TSC
//            builderClassifier.invoke(c,data);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        if(cvFlag.equals("1")){
//            double[][] a=ClassifierTools.crossValidationWithStats((Classifier) c,data, 10);
//            System.out.println("ROTF ACC = "+a[0][0]);
//        }
//
//        //write obj to file
//        FileOutputStream f =new FileOutputStream(new File(moduleAddress));
//        ObjectOutputStream o =new ObjectOutputStream(f);
//        o.writeObject(c);
//        o.close();
//        f.close();
//        return c;
//    }
//    /**
//     *
//     * @param params
//     * params[0]: the address of classifer
//     * params[1]: data instance address
//     * @return
//     */
//    public double[] predict(String[] params) throws Exception {
//        String moduleAddress =params[0];
//        String dataAddress=params[1];
//        Instances testData = ClassifierTools.loadData(dataAddress);
//        Classifier c=null;
//        //read obj from file
//        FileInputStream fi = new FileInputStream(new File(moduleAddress));
//        ObjectInputStream oi = new ObjectInputStream(fi);
//        try {
//            c=(Classifier) oi.readObject();
//        } catch (ClassNotFoundException e) {
//            System.out.println("there is no classifiers on this path");
//        }
//        oi.close();
//        fi.close();
//        double[] results=new double[testData.numInstances()];
//        for(int i=0;i<testData.numInstances();i++){
//            results[i]=c.classifyInstance(testData.instance(i));}
//        return results;
//    }
//
//
//    /**
//     * Training the classifier with define the hyper parameter
//     * @param params
//     * params[0]: the address of data
//     * params[1]: the address to store the module
//     * params[2]: the name of classifiers
//     * params[3]: 0- train without cross validation, 1-train with cross validation
//     * params[4]: name of method to set the hyperparameter
//     * params[5]: value of hyperparameter
//     * @return the builded classifiers and write to
//     */
//    public Object trainWithHyper(String[] params) throws IOException {
//        String dataAddress=params[0];
//        String moduleAddress=params[1];
//        String classiferName=params[2];
//        String cvFlag = params[3];
//        String[] hyperparams = params[4].split("_");
//
//        Instances data = ClassifierTools.loadData(dataAddress);  // training data
//        Object c=null;
//        try {
//            Class algorithm=Class.forName(classiferName);  // reflection out the class
//            c=algorithm.newInstance();
//            Method[] methods=algorithm.getMethods();
//
//            for (int i = 0; i<hyperparams.length; ){
//                // get the method name to set specific hyper params
//                String methodName = hyperparams[i];
//                for (Method method : methods){
//                    if(method.getName().equals(methodName)){
//                        Type[] types=method.getParameterTypes();
//                        for (Type type : types){
//                            switch (type.getTypeName()){
//                                case "float":{
//                                    float value = Float.valueOf(hyperparams[i+1]);
//                                    method.invoke(c,value);
//                                    break;
//                                }
//                                case "int":{
//                                    int value = Integer.valueOf(hyperparams[i+1]);
//                                    method.invoke(c,value);
//                                    break;
//
//                                }
//                                case "java.lang.String":{
//                                    String value = hyperparams[i+1];
//                                    method.invoke(c,value);
//                                    break;
//                                }
//                                default:
//                            }
//                        }
//                    }
//                }
//                i++;
//            }
//            Method builderClassifier=algorithm.getMethod("buildClassifier",data.getClass());
//            // buidClassifier is a method shared across TSC
//            builderClassifier.invoke(c,data);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        if(cvFlag.equals("1")){
//            double[][] a=ClassifierTools.crossValidationWithStats((Classifier) c,data, 10);
//            System.out.println("ROTF ACC = "+a[0][0]);
//        }
//
//        //write obj to file
//        FileOutputStream f =new FileOutputStream(new File(moduleAddress));
//        ObjectOutputStream o =new ObjectOutputStream(f);
//        o.writeObject(c);
//        o.close();
//        f.close();
//        return c;
//    }
}