package album

import "net/http"
func getAlbums(c *gin.Context) {
	c.IndentedJSON(http.StatusOK, albums)
}

