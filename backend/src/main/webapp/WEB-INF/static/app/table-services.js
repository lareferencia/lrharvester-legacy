angular
		.module('table.services', [ "ngTable", "ngResource" ])
		.service(
				'TableSrv',
				[
						"NgTableParams",
						"$resource",

						function(NgTableParams, $resource) {

							/**
							 * Crea una tabla partiendo de la URL de un WS y una
							 * función de cálculo de longitud de datos URL: la
							 * url del ws COUNT_FUNCTION: Función que recibe
							 * como parámetro la data y devuelve un int con la
							 * logitud de los datos
							 */
							this.createNgTableFromWsURL = function(url,
									count_function) {

								var Api = $resource(url);

								return new NgTableParams(
										{},
										{
											getData : function(params) {
												return Api.get(params.url()).$promise
														.then(function(data) {
															params
																	.total(count_function(data));
															return data.networks;
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

