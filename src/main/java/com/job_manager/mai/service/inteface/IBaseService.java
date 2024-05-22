package com.job_manager.mai.service.inteface;

public interface IBaseService<Request, C extends Request, U extends Request, D extends Request, TypeId> extends ICrudService<Request, C, U, D, TypeId>, IUtilService {

}
