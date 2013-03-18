/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oracle.javaee7.samples.batch.chunk;

import java.io.Serializable;
import java.util.List;
@javax.inject.Named("SimpleItemWriter")
public class SimpleItemWriter
    implements javax.batch.api.chunk.ItemWriter<String> {
    
    @Override
    public void open(Serializable e) throws Exception {
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void writeItems(List<String> list) throws Exception {
        StringBuilder sb = new StringBuilder("SimpleItemWriter:");
        for (String s : list) {
            sb.append(" ").append(s);
        }
        System.out.println(sb.toString());
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        return null;
    }
    
}
