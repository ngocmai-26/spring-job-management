package com.job_manager.mai.response.job;

import com.job_manager.mai.model.Job;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Data
@Component
public class JobResponse extends Job implements IJobMapper {
    @Override
    public Collection<JobResponse> mapTo(Collection<Job> source) {
        return null;
    }

    @Override
    public JobResponse mapTo(Job source) {
        JobResponse jobResponse = mapper.map(source, JobResponse.class);
        return jobResponse.isDisplay() ? jobResponse : null;
    }

    @Override
    public Page<JobResponse> mapTo(Page<Job> source) {
        return source.map(this::mapTo);
    }
}
