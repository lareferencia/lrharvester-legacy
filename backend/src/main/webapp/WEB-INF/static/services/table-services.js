angular
		.module('table.services', [ "ngTable", "ngResource", 'data.services' ])
		.service(
				'TableSrv', 
				[
						"NgTableParams",
						"$resource",
						"DataSrv",

						function(NgTableParams, $resource, DataSrv) {

							/**
							 * Crea una tabla partiendo de la URL de un WS y una
							 * función de cálculo de longitud de datos URL: la
							 * url del ws COUNT_FUNCTION: Función que recibe
							 * como parámetro la data y devuelve un int con la
							 * logitud de los datos
							 */
							this.createNgTableFromWsURL = function(url, parameters_function, initialPageNumber, initialCount, countsArray ) {

								if (initialPageNumber == null)
									initialPageNumber = 1;
								
								if (initialCount == null)
									initialCount = 20;
								
								if (countsArray == null)
									countsArray = [10, 25, 50, 100];
								
								var Api = $resource(url);

								return new NgTableParams(
										{page:initialPageNumber, count:initialCount},
										{
											counts : countsArray,
											getData : function(params) {
												return Api.get(params.url()).$promise
														.then(function(data) {
															
															var p = parameters_function(data);
															
															if (p.total != null) params.total(p.total);
															//if (p.count != null) params.count(p.count);
															
															return p.data ;
														});
											}, /* fin getData */
										// paginationMaxBlocks: 13,
										// paginationMinBlocks: 2,

										});

							};

							/**
							 * Crea una tabla partiendo de un array data_array:
							 * el array con los datos
							 */
							this.createNgTableFromArray = function(data_array) {

								return new NgTableParams({}, {
									dataset : data_array
								});

							};
							
							/**
							 * Crea una tabla partiendo de un array data_array:
							 * el array con los datos
							 */
							this.createNgTableFromGetData = function(get_data) {

								return new NgTableParams({}, {
									getData : get_data
								});

							};
							
				

						} ]);

