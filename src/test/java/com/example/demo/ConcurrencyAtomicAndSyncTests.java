package com.example.demo;

import com.sun.istack.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrencyAtomicAndSyncTests {

    // ------------ Synchronized -------------

    static final Class classOne = new Class();

    static final class Class{

        // assume one class for simplicity
        final int CAPACITY_LIMIT = 5;

        final String id = "123";

        private int currentCapacity = 0;

        void incrementCurrentCapacity(){
            this.currentCapacity++;
        }

        int getCurrentCapacity(){
            return this.currentCapacity;
        }

    }

    static final class AdmissionManager{

        private final Map<String, Class> classMap = new ConcurrentHashMap<String, Class>(){{
            put(classOne.id, classOne);
        }};

        private static AdmissionManager admissionManager;

        private AdmissionManager(){}

        public static @NotNull AdmissionManager getInstance(){
            if(admissionManager == null)
                admissionManager = new AdmissionManager();

            return admissionManager;
        }

        public boolean tryToAdmit(@NotNull final String classID){
            final Class classToAdmitTo = this.classMap.get(classID);

            if(classToAdmitTo == null)
                return false;

            synchronized (classToAdmitTo){

                if(classToAdmitTo.CAPACITY_LIMIT - classToAdmitTo.getCurrentCapacity() == 0)
                    return false;

                classToAdmitTo.incrementCurrentCapacity();

                return true;
            }
        }
    }

    @Test
    public void testSynchronizedSolution(){
        try{

            final List<Future<Boolean>> futureList = new ArrayList<>();

            final ExecutorService executorService = Executors.newFixedThreadPool(5);

            int amountAddedSuccessfully = 0;

            for(int i = 0; i < classOne.CAPACITY_LIMIT * 2; i++)
                futureList.add(
                        executorService.submit(()->AdmissionManager.getInstance().tryToAdmit(classOne.id))
                );

            for(final Future<Boolean> future : futureList)
                if(future.get())
                    amountAddedSuccessfully++;

            Assertions.assertEquals(
                    classOne.CAPACITY_LIMIT,
                    amountAddedSuccessfully
            );

        }
        catch(InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
    }

    // ------------- Atomic --------------------

    static final ClassAtomic classOneAtomic = new ClassAtomic();

    static final class ClassAtomic{

        // assume one class for simplicity
        final int CAPACITY_LIMIT = 5;

        final String id = "123";

        private final AtomicInteger currentCapacity = new AtomicInteger(0);

        public AtomicInteger getCurrentCapacity() {
            return this.currentCapacity;
        }
    }

    static final class AdmissionManagerAtomic{

        private final Map<String, ClassAtomic> classMap = new ConcurrentHashMap<String, ClassAtomic>(){{
            put(classOneAtomic.id, classOneAtomic);
        }};

        private static AdmissionManagerAtomic admissionManager;

        private AdmissionManagerAtomic(){}

        public static @NotNull AdmissionManagerAtomic getInstance(){
            if(admissionManager == null)
                admissionManager = new AdmissionManagerAtomic();

            return admissionManager;
        }

        public boolean tryToAdmit(@NotNull final String classID){
            final ClassAtomic classToAdmitTo = this.classMap.get(classID);

            if(classToAdmitTo == null)
                return false;

            final AtomicInteger atomicInteger = classToAdmitTo.getCurrentCapacity();

            boolean wasCompleted;

            do{

                int currentCapacity = atomicInteger.get();

                if(classToAdmitTo.CAPACITY_LIMIT - currentCapacity == 0)
                    return false;

                wasCompleted = atomicInteger.compareAndSet(currentCapacity, currentCapacity + 1);

            }while(!wasCompleted);

            return true;
        }
    }

    @Test
    public void testAtomicSolution(){
        try{

            final List<Future<Boolean>> futureList = new ArrayList<>();

            final ExecutorService executorService = Executors.newFixedThreadPool(5);

            int amountAddedSuccessfully = 0;

            for(int i = 0; i < classOneAtomic.CAPACITY_LIMIT * 2; i++)
                futureList.add(
                        executorService.submit(()->AdmissionManagerAtomic.getInstance().tryToAdmit(classOneAtomic.id))
                );

            for(final Future<Boolean> future : futureList)
                if(future.get())
                    amountAddedSuccessfully++;

            Assertions.assertEquals(
                    classOneAtomic.CAPACITY_LIMIT,
                    amountAddedSuccessfully
            );

        }
        catch(InterruptedException | ExecutionException e){
            System.out.println(e.getMessage());
        }
    }

    /*
    My analysis:
    Synchronized: operates on first come, first serve manner (fair) but is slower due to bottle-necking
    Atomic: does not operate on a first come, first serve manner (unfair) but is faster due to not bottle-necking
     */

}
