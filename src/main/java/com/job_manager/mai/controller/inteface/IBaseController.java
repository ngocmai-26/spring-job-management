package com.job_manager.mai.controller.inteface;

public interface IBaseController<Request, C extends Request, U extends Request, D extends Request, TypeId> extends ICrudController<Request, C, U, D, TypeId>, ISearchController, ISortController {
}
