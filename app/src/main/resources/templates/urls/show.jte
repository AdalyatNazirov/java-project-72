@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.main(
title = "URL Details",
content = @`
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Website Analytics</h1>
        <div>
            <a href="/urls" class="btn btn-outline-secondary">Return to Dashboard</a>
        </div>
    </div>

    <div class="card mb-4">
        <div class="card-header">
            <h5 class="card-title mb-0">Website Profile</h5>
        </div>
        <div class="card-body">
            <table class="table table-bordered mb-0">
                <tr>
                    <th style="width: 200px">ID</th>
                    <td>${page.getUrl().getId()}</td>
                </tr>
                <tr>
                    <th>Website</th>
                    <td><a href="${page.getUrl().getName()}">${page.getUrl().getName()}</a></td>
                </tr>
                <tr>
                    <th>Added On</th>
                    <td>${page.getUrl().getFormattedCreatedAt()}</td>
                </tr>
            </table>
        </div>
    </div>

    <h2>Checks</h2>

    <div class="alert alert-info">No checks performed yet</div>
`
)
